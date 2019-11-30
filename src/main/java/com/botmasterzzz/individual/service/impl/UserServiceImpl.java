package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.dto.AbstractDto;
import com.botmasterzzz.individual.dto.UserDTO;
import com.botmasterzzz.individual.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private KafkaTemplate<Long, AbstractDto> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${user.topic.name}")
    private String userTopicName;

    @Value(value = "${user.password.topic.name}")
    private String userPasswordTopicName;

    @Override
    public UserDTO save(UserDTO userDTO) {
        return null;
    }

    @Override
    @Async
    public void send(UserDTO userDTO) {
        LOGGER.info("<= sending {}", writeValueAsString(userDTO));
    }

    @Override
    @Async
    public void userPictureUrlUpdate(UserDTO userDTO) {
        LOGGER.info("<= sending {}", writeValueAsString(userDTO));
        kafkaTemplate.send(userTopicName, userDTO);
    }

    @Override
    @Async
    public void userPasswordUpdate(UserDTO userDTO) {
        LOGGER.info("<= sending {}", writeValueAsString(userDTO));
        kafkaTemplate.send(userPasswordTopicName, userDTO);
    }

    @Override
    public void consume(UserDTO userDTO) {
        LOGGER.info("=> consumed {}", writeValueAsString(userDTO));
    }

    private String writeValueAsString(UserDTO userDTO) {
        try {
            return objectMapper.writeValueAsString(userDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + userDTO.toString());
        }
    }
}
