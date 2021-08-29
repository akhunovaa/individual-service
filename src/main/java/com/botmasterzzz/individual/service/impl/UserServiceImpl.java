package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.dto.IndividualDTO;
import com.botmasterzzz.individual.dto.PasswordDTO;
import com.botmasterzzz.individual.dto.UserDTO;
import com.botmasterzzz.individual.dto.UserProfileDTO;
import com.botmasterzzz.individual.entity.Individual;
import com.botmasterzzz.individual.entity.User;
import com.botmasterzzz.individual.exception.InvalidPasswordException;
import com.botmasterzzz.individual.repository.UserDao;
import com.botmasterzzz.individual.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    //
//    @Autowired
//    private KafkaTemplate<Long, AbstractDto> kafkaTemplate;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Value(value = "${user.topic.name}")
    private String userTopicName;
    @Value(value = "${user.password.topic.name}")
    private String userPasswordTopicName;
    @Value(value = "${user.data.topic.name}")
    private String userDataTopicName;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        Long userId = userDTO.getId();
        Individual individual = userDao.findIndividualById(userId).orElse(new Individual());
        LOGGER.info("Request for a picture update to user: {}", writeValueAsString(individual));
        LOGGER.info("<= sending {}", writeValueAsString(userDTO));
        individual.setImageUrl(userDTO.getImageUrl());
        individual.setId(userId);
        userDao.individualUpdate(individual);
    }

    @Override
    @Async
    public void userPasswordUpdate(UserDTO userDTO) {
        LOGGER.info("<= sending {}", writeValueAsString(userDTO));
        //kafkaTemplate.send(userPasswordTopicName, userDTO);
    }

    @Override
    public void userPasswordUpdate(PasswordDTO passwordDTO) {
        Long userId = passwordDTO.getId();
        User user = userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));
        LOGGER.info("Request for a password update to user: {}", writeValueAsString(user));
        if (!passwordEncoder.matches(passwordDTO.getPasswordMain(), user.getPassword())) {
            LOGGER.error("Requested password {} is not valid for a user: {}", passwordDTO.getPasswordMain(), writeValueAsString(user));
            throw new InvalidPasswordException("Введенный  пароль неверен");
        }
        LOGGER.info("<= sending {}", writeValueAsString(passwordDTO));
        user.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        userDao.userUpdate(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "individual-data", key = "#individualDTO.id"),
            @CacheEvict(value = "user-data", key = "#individualDTO.id"),
            @CacheEvict(value = "user-info-data", key = "#individualDTO.login")
    })
    public void individualUpdate(IndividualDTO individualDTO) {
        Long userId = individualDTO.getId();
        LOGGER.info("Request for a user info update to user: {}", writeValueAsString(individualDTO));
        Individual individual = userDao.findIndividualById(userId).orElse(new Individual());
        User user = userDao.findById(userId).orElse(new User());
        individualDTO2EntityMerge(individual, individualDTO);
        user.setEmail(individualDTO.getEmail());
        individual.setUser(user);
        LOGGER.info("<= sending {}", writeValueAsString(individual));
        userDao.individualUpdate(individual);
    }

    @Override
    public void consume(UserDTO userDTO) {
        LOGGER.info("=> consumed {}", writeValueAsString(userDTO));
    }

    @Override
    @Cacheable(value = "user-data", key = "#id")
    public UserDTO findUser(Long id) {
        User user = userDao.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
        LOGGER.info("=> consumed {}", user.getLogin());
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setEmailVerified(user.isEmailVerified());
        userDTO.setImageUrl(user.getImageUrl());
        userDTO.setLogin(user.getLogin());
        userDTO.setName(user.getName());
        userDTO.setNote(user.getNote());
        userDTO.setPatrName(user.getPatrName());
        userDTO.setPhone(user.getPhone());
        userDTO.setProvider(user.getProvider().name());
        userDTO.setRoleId(user.getUserRole().getId());
        userDTO.setSurname(user.getSurname());
        userDTO.setId(user.getId());
        LOGGER.info("User was invoked with login: {}", userDTO.getLogin());
        return userDTO;
    }

    @Override
    @Cacheable(value = "individual-data", key = "#id")
    public IndividualDTO findIndividual(Long id) {
        Optional<Individual> individualI = userDao.findIndividualById(id);
        Individual individual = individualI.orElseGet(Individual::new);
        IndividualDTO individualDTO = new IndividualDTO();
        individualEntity2DTOMerge(individual, individualDTO);
        individualDTO.setId(id);
        LOGGER.info("Individual was invoked: {}", writeValueAsString(individualDTO));
        return individualDTO;
    }

    @Override
    @Cacheable(value = "user-info-data", key = "#username")
    public UserProfileDTO findUserProfile(String username){
        Individual individual;
        Optional<Individual> optionalIndividual = userDao.findIndividualByNickName(username);

        if (optionalIndividual.isPresent()) {
            individual = optionalIndividual.get();
        } else {
            if (!username.trim().toLowerCase().contains("id")) {
                throw new UsernameNotFoundException("User " + username + " not found");
            }
            Long id;
            try {
                id = Long.parseLong(username.trim().toLowerCase().split("id")[1]);
            } catch (NumberFormatException exception) {
                throw new UsernameNotFoundException("User " + username + " not found");
            }
            individual = userDao.findIndividualById(id).orElseThrow(() -> new UsernameNotFoundException("User " + id + " not found"));
        }
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        individualEntity2DTOMerge(individual, userProfileDTO);
        userProfileDTO.setId(individual.getId());
        LOGGER.info("User was invoked: {}", writeValueAsString(userProfileDTO));
        return userProfileDTO;
    }

    private String writeValueAsString(UserDTO userDTO) {
        try {
            return objectMapper.writeValueAsString(userDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + userDTO.toString());
        }
    }

    private String writeValueAsString(PasswordDTO passwordDTO) {
        try {
            return objectMapper.writeValueAsString(passwordDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + passwordDTO.toString());
        }
    }

    private String writeValueAsString(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + user.toString());
        }
    }

    private String writeValueAsString(IndividualDTO individualDTO) {
        try {
            return objectMapper.writeValueAsString(individualDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + individualDTO.toString());
        }
    }

    private String writeValueAsString(Individual individual) {
        try {
            return objectMapper.writeValueAsString(individual);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + individual.toString());
        }
    }

    private String writeValueAsString(UserProfileDTO userProfileDTO) {
        try {
            return objectMapper.writeValueAsString(userProfileDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + userProfileDTO.toString());
        }
    }

    private void individualDTO2EntityMerge(Individual individual, IndividualDTO individualDTO) {
        User user = new User();
        user.setId(individualDTO.getId());
        user.setEmail(individualDTO.getEmail());
        individual.setId(individualDTO.getId());
        individual.setName(individualDTO.getName());
        individual.setSurname(individualDTO.getSurname());
        individual.setPatrName(individualDTO.getPatrName());
        individual.setNickname(individualDTO.getNickName());
        individual.setPhone(individualDTO.getPhone());
        individual.setNickname(individualDTO.getNickName());
        if (null != individualDTO.getBirthDate()) {
            individual.setBirthDate(new Date(individualDTO.getBirthDate().getTime()));
        }
        individual.setGender(individualDTO.getGender());
        individual.setLanguage(individualDTO.getLanguage());
        individual.setCity(individualDTO.getCity());
        individual.setInfo(individualDTO.getInfo());
        individual.setUser(user);
    }

    private void individualEntity2DTOMerge(Individual individual, IndividualDTO individualDTO) {
        individualDTO.setName(individual.getName());
        individualDTO.setSurname(individual.getSurname());
        individualDTO.setPatrName(individual.getPatrName());
        individualDTO.setNickName(individual.getNickname());
        individualDTO.setPhone(individual.getPhone());
        individualDTO.setBirthDate(individual.getBirthDate());
        individualDTO.setGender(individual.getGender());
        individualDTO.setLanguage(individual.getLanguage());
        individualDTO.setCity(individual.getCity());
        individualDTO.setInfo(individual.getInfo());
        individualDTO.setImageUrl(individual.getImageUrl());
        individualDTO.setEmail(individual.getUser().getEmail());
    }

    private void individualEntity2DTOMerge(Individual individual, UserProfileDTO userProfileDTO) {
        userProfileDTO.setName(individual.getName());
        userProfileDTO.setSurname(individual.getSurname());
        userProfileDTO.setPatrName(individual.getPatrName());
        userProfileDTO.setNickName(individual.getNickname());
        userProfileDTO.setBirthDate(individual.getBirthDate());
        userProfileDTO.setGender(individual.getGender());
        userProfileDTO.setLanguage(individual.getLanguage());
        userProfileDTO.setCity(individual.getCity());
        userProfileDTO.setInfo(individual.getInfo());
        userProfileDTO.setImageUrl(individual.getImageUrl());
    }
}
