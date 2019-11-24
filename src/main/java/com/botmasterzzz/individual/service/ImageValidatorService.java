package com.botmasterzzz.individual.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageValidatorService {

    void validate(MultipartFile file);

}
