package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.exception.ImageValidationException;
import com.botmasterzzz.individual.service.ImageValidatorService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageValidatorServiceImpl implements ImageValidatorService {

    public static final String PNG_MIME_TYPE = "image/png";
    public static final String JPEG_MIME_TYPE = "image/jpeg";
    public static final long TEN_MB_IN_BYTES = 5242880;

    @Override
    public void validate(MultipartFile target) {
        MultipartFile file = target;

        if (file.isEmpty()) {
            throw new ImageValidationException("Загружаемый файл не должен быть пустым");
        } else if (file.getSize() > TEN_MB_IN_BYTES) {
            throw new ImageValidationException("Загружаемый размер файла первышает допустимые нормы");
        } else if (!PNG_MIME_TYPE.equalsIgnoreCase(file.getContentType()) && !JPEG_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
            throw new ImageValidationException("Загружаемый файл не соответствует допустимым нормам");
        }
    }
}
