package com.botmasterzzz.individual.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void storeMainImage(MultipartFile file, long userId);

    Stream<Path> loadAll();

    Path load(long userId);

    Resource loadAsResource(String filename);

    void deleteAll();
}
