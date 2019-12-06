package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    @CacheEvict(value = "image", key = "#userId")
    public void storeMainImage(MultipartFile file, long userId) {
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
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                LOGGER.error("Cat not upload a {}", fileName, e);
            }
        } else {
            LOGGER.error("Cat not upload a {}" + fileName + " because of empty file");
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    @Cacheable(value = "image", key = "#userId")
    public Path load(long userId) {
        String fullPath = path + "/images";
        String usersPathLocation = fullPath + "/user/" + userId;
        LOGGER.info("User path location directory {}", usersPathLocation);
        String[] files = FileUtils.getFile(new File(usersPathLocation)).exists() ? FileUtils.getFile(new File(usersPathLocation)).list() : new String[]{"image.jpeg"};
        File imageFile = null;
        for (String file : files) {
            imageFile = FileUtils.getFile(new File(usersPathLocation), file);
        }
        LOGGER.info("User file {} in  directory {}", imageFile.getName(), usersPathLocation);
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
