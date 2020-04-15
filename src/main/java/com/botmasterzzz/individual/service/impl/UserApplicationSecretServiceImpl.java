package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.dto.UserApplicationSecretDTO;
import com.botmasterzzz.individual.entity.User;
import com.botmasterzzz.individual.entity.UserApplicationSecretEntity;
import com.botmasterzzz.individual.repository.UserApplicationSecretDao;
import com.botmasterzzz.individual.repository.UserDao;
import com.botmasterzzz.individual.service.UserApplicationSecretService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserApplicationSecretServiceImpl implements UserApplicationSecretService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserApplicationSecretService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserApplicationSecretDao userApplicationSecretDao;

    @Autowired
    private StandardPBEStringEncryptor stringEncrypt;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserApplicationSecretDTO createNewUserApplicationSecret(Long userId, String secretName) {
        User user = userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));
        LOGGER.info("Request for a secret create to user: {}", writeValueAsString(user));
        String data2Encrypt = userId + "_" + user.getLogin() + "_" + secretName;
        String secretText = stringEncrypt.encrypt(data2Encrypt);
        UserApplicationSecretEntity userApplicationSecretEntity = new UserApplicationSecretEntity();
        userApplicationSecretEntity.setUser(user);
        userApplicationSecretEntity.setName(secretName);
        userApplicationSecretEntity.setPlainValue(data2Encrypt);
        userApplicationSecretEntity.setValue(secretText);
        userApplicationSecretDao.userApplicationSecretSave(userApplicationSecretEntity);
        return UserApplicationSecretDTO.newBuilder()
                .setName(secretName)
                .setCreatedTimeStamp(new Date().getTime())
                .setIsBanned(false)
                .build();
    }

    @Override
    @Cacheable(value = "user-application-secret", key = "#userId + #limit")
    public List<UserApplicationSecretDTO> getUserApplicationSecretList(Long userId, int limit) {
        User user = userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));
        LOGGER.info("Request for a secret list for user: {}", writeValueAsString(user));
        List<UserApplicationSecretEntity> userApplicationSecretEntityList = userApplicationSecretDao.userApplicationSecretList(userId, limit);
        List<UserApplicationSecretDTO> applicationSecretDTOList = new ArrayList<>(userApplicationSecretEntityList.size());
        for (UserApplicationSecretEntity userApplicationSecretEntity : userApplicationSecretEntityList) {
            UserApplicationSecretDTO userApplicationSecretDTO = new UserApplicationSecretDTO();
            userApplicationSecretDTO.setName(userApplicationSecretEntity.getName());
            userApplicationSecretDTO.setBanned(userApplicationSecretEntity.isBanned());
            userApplicationSecretDTO.setValue(userApplicationSecretEntity.getValue());
            userApplicationSecretDTO.setCreatedTimestamp(userApplicationSecretEntity.getAudWhenCreate().getTime());
            applicationSecretDTOList.add(userApplicationSecretDTO);
        }
        return applicationSecretDTOList;
    }

    private String writeValueAsString(UserApplicationSecretDTO userApplicationSecretDTO) {
        try {
            return objectMapper.writeValueAsString(userApplicationSecretDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + userApplicationSecretDTO.toString());
        }
    }

    private String writeValueAsString(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + user.toString());
        }
    }
}
