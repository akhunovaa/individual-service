package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.dto.ImageDTO;
import com.botmasterzzz.individual.dto.UserPrincipal;
import com.botmasterzzz.individual.exception.ImageNotFoundException;
import com.botmasterzzz.individual.model.Response;
import com.botmasterzzz.individual.service.ImageValidatorService;
import com.botmasterzzz.individual.service.StorageService;
import com.botmasterzzz.individual.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

@RestController
public class IndividualResourceController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualResourceController.class);

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageValidatorService imageValidatorService;

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/image/{userId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> userImageGet(@PathVariable int userId) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> responseEntity;
        Path imagePath = storageService.load((long) userId);
        if (!imagePath.toFile().exists()) {
            LOGGER.info("File {} not found", imagePath.toString());
            throw new ImageNotFoundException("Данный файл отсутствует");
        }
        LOGGER.info("File path {} loaded", imagePath.toString());
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String mimeType = fileTypeMap.getContentType(imagePath.toFile());
        switch (mimeType) {
            case "image/png":
                headers.setContentType(MediaType.IMAGE_PNG);
                break;
            default:
                headers.setContentType(MediaType.IMAGE_JPEG);
        }
        BufferedImage img;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            img = ImageIO.read(new FileInputStream(imagePath.toFile()));
            BufferedImage resized = ImageUtil.resize(img, 300, 300);
            ImageIO.write(resized, "jpg", bos);
        } catch (IOException e) {
            LOGGER.error("Error occurs during writing {}", imagePath.toString(), e);
        }
        byte[] media = bos.toByteArray();
        responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    @PreAuthorize("authenticated")
    public Response userImageUpload(@RequestParam("file") MultipartFile file) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        String requestUrl = "https://botmasterzzz.com/individual/";
        imageValidatorService.validate(file);
        storageService.storeMainImage(file, userPrincipal.getId());
        String imageUrl = requestUrl + "user/" + userPrincipal.getId() + "/image";
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(userPrincipal.getId());
        imageDTO.setImageUrl(imageUrl);
        //securityUserService.userImageUrlUpdate(imageDTO);
        return getResponseDto(imageDTO);
    }
}
