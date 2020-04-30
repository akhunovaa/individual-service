package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.service.StorageService;
import com.botmasterzzz.individual.util.ImageUtil;
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
import java.util.Objects;
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
//    @CacheEvict(value = "user-picture", key = "#userId")
    public String storeMainImage(MultipartFile file, Long userId) {
        String fullPath = path + "/images";
        String usersPathLocation = fullPath + "/user/" + userId;
        File fileX = new File(usersPathLocation);
        int filesCount;
        try {
            if (!fileX.exists()) {
                FileUtils.forceMkdir(new File(usersPathLocation));
            }
        } catch (IOException e) {
            LOGGER.error("Can not create dirs for a user {} path {} location", userId, usersPathLocation, e);
        }
        filesCount = Objects.requireNonNull(fileX.listFiles()).length;
        String fileExtension = null != file.getContentType() ? file.getContentType().split("/")[1] : "jpg";
        String fileName = usersPathLocation + "/" + (filesCount + 1) + "-image." + fileExtension;
        if (!file.isEmpty()) {
            BufferedOutputStream stream = null;
            try {
                byte[] bytes = file.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                LOGGER.error("Cat not upload a {}", fileName, e);
            } finally {
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
        return fileName;
    }

    @Override
//    @Cacheable(value = "user-picture", key = "#userId")
    public byte[] getByteArrayOfTheImage(File image, HttpHeaders headers, Long userId, int width, int height) {
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
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            img = ImageIO.read(new FileInputStream(image));
             BufferedImage resized = ImageUtil.resize(img, height, width);
             ImageIO.write(resized, "jpg", bos);
            imageBytes = bos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Error occurs during writing {} to {}", image.getName(), image.getAbsolutePath(), e);
        }
        return imageBytes;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(long userId) {
        String fullPath = path + "/images";
        String usersPathLocation = fullPath + "/user/" + userId;
        File fileX = new File(usersPathLocation);
        int filesCount = Objects.requireNonNull(fileX.listFiles()).length;
        LOGGER.info("User path location directory {}", usersPathLocation);
        File imageFile = Stream.of(Objects.requireNonNull(fileX.listFiles())).filter(file -> file.getName().contains(filesCount + "-image")).findFirst().get();
        LOGGER.info("User file {} in  directory {}", imageFile, usersPathLocation);
        return imageFile.toPath();
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
