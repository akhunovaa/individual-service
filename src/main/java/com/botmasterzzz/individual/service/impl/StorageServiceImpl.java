package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    @Value("${multipart.file.upload.path}")
    private String path;

    @Override
    @PostConstruct
    public void init() {
        String fullPath = path + "/images";
        try {
            FileUtils.forceMkdir(new File(fullPath));
        } catch (IOException e) {
            LOGGER.error("Can not create dirs for a repository path location {}", fullPath, e);
        }
    }

    @Override
    @CacheEvict(value = "bytesFromPersistentVolume", key = "#userId")
    public void storeMainImage(MultipartFile file, Long userId) {
        String fullPath = path + "/images";
        String usersPathLocation = fullPath + "/user/" + userId;
        try {
            FileUtils.forceMkdir(new File(usersPathLocation));
        } catch (IOException e) {
            LOGGER.error("Can not create dirs for a user {} path {} location", userId, usersPathLocation, e);
        }
        String fileExtension = null != file.getContentType() ? file.getContentType().split("/")[1] : "jpg";
        String fileName = usersPathLocation + "/" + "image." + fileExtension;
        File fileJpg = new File(usersPathLocation + "/" + "image." + "jpg");
        File fileJpeg = new File(usersPathLocation + "/" + "image." + "jpeg");
        File filePng = new File(usersPathLocation + "/" + "image." + "png");
        if (fileJpg.exists()) {
            FileUtils.deleteQuietly(fileJpg);
        }
        if (filePng.exists()) {
            FileUtils.deleteQuietly(filePng);
        }
        if (fileJpeg.exists()) {
            FileUtils.deleteQuietly(fileJpeg);
        }
        if (!file.isEmpty()) {
            BufferedOutputStream stream = null;
            try {
                byte[] bytes = file.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                LOGGER.error("Cat not upload a {}", fileName, e);
            }finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("Stream closing error", e);
                }
            }
        } else {
            LOGGER.error("Cat not upload a {}" + fileName + " because of empty file");
        }
    }

    @Override
    @Cacheable(value = "bytesFromPersistentVolume", key = "#userId")
    public byte[] getByteArrayOfTheImage(File image, HttpHeaders headers, Long userId) {
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String mimeType = fileTypeMap.getContentType(image);
        switch (mimeType) {
            case "image/png":
                headers.setContentType(MediaType.IMAGE_PNG);
                break;
            default:
                headers.setContentType(MediaType.IMAGE_JPEG);
        }
        BufferedImage img;
        byte[] imageBytes = new byte[0];
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            img = ImageIO.read(new FileInputStream(image));
//            BufferedImage resized = ImageUtil.resize(img, 300, 300);
            ImageIO.write(img, "jpg", bos);
            imageBytes = bos.toByteArray();
        }catch (IOException e) {
            LOGGER.error("Error occurs during writing {} to {}", image.getName(), image.getAbsolutePath(), e);
        }
        return imageBytes;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    @Cacheable(value = "imagePath", key = "#userId")
    public Path load(long userId) {
        String fullPath = path + "/images";
        String usersPathLocation = fullPath + "/user/" + userId;
        LOGGER.info("User path location directory {}", usersPathLocation);
        String[] files = FileUtils.getFile(new File(usersPathLocation)).exists() ? FileUtils.getFile(new File(usersPathLocation)).list() : new String[]{"image.jpeg"};
        File imageFile = null;
        for (String file : files != null ? files : new String[0]) {
            imageFile = FileUtils.getFile(new File(usersPathLocation), file);
        }
        String imageFileName = null != imageFile ? imageFile.getName() : "empty-file-name";
        LOGGER.info("User file {} in  directory {}", imageFileName, usersPathLocation);
        return imageFile != null ? imageFile.toPath() : null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
