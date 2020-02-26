package com.botmasterzzz.individual.service;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String storeMainImage(MultipartFile file, Long userId);

    byte[] getByteArrayOfTheImage(File image, HttpHeaders headers, Long userId);

    Stream<Path> loadAll();

    Path load(long userId);

    Resource loadAsResource(String filename);

    void deleteAll();
}
