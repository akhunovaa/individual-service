package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.dto.ImageDTO;
import com.botmasterzzz.individual.dto.UserDTO;
import com.botmasterzzz.individual.dto.UserPrincipal;
import com.botmasterzzz.individual.exception.ImageNotFoundException;
import com.botmasterzzz.individual.model.Response;
import com.botmasterzzz.individual.service.ImageValidatorService;
import com.botmasterzzz.individual.service.StorageService;
import com.botmasterzzz.individual.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping(value = "/image")
public class IndividualResourceController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualResourceController.class);

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageValidatorService imageValidatorService;

    @RequestMapping(value = "/{userId}/{width}/{height}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> userImageGet(@PathVariable String userId, @PathVariable String width, @PathVariable String height) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> responseEntity;
        Long iUserId = Long.valueOf(userId);
        Path imagePath = storageService.load(iUserId);
        if (Files.notExists(imagePath)) {
            LOGGER.info("File {} not found", imagePath.toString());
            throw new ImageNotFoundException("Данный файл отсутствует");
        }
        LOGGER.info("File path {} loaded", imagePath.toString());
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.set("Requested-User", userId);
        int intWidth, intHeight;
        try {
            intWidth = Integer.parseInt(width);
            intHeight = Integer.parseInt(height);
        }catch (NumberFormatException exception){
            throw new ImageNotFoundException("Image not found");
        }
        if (intWidth >= 2000 || intHeight >= 2000){
            throw new ImageNotFoundException("Image not found");
        }
        byte[] media = storageService.getByteArrayOfTheImage(imagePath.toFile(), headers, iUserId, intWidth, intHeight);
        responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @PreAuthorize("authenticated")
    public Response userImageUpload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        String requestUrl = httpServletRequest.getScheme() + "s://" + httpServletRequest.getServerName() + "/individual";
        imageValidatorService.validate(file);
        String uploadedImageAbsolutePath = storageService.storeMainImage(file, userPrincipal.getId(), userPrincipal.getLogin());
        String imageUrl = requestUrl + "/image/" + userPrincipal.getId();
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(userPrincipal.getId());
        imageDTO.setImageUrl(imageUrl);
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(userPrincipal.getLogin());
        userDTO.setEmail(userPrincipal.getEmail());
        userDTO.setId(userPrincipal.getId());
        userDTO.setImageUrl(imageUrl);
        userService.userPictureUrlUpdate(userDTO);
        return getResponseDto(imageDTO);
    }
}
