package com.botmasterzzz.individual.service;

import com.botmasterzzz.individual.dto.UserDTO;

public interface UserService {

    UserDTO save(UserDTO userDTO);

    void send(UserDTO userDTO);

    void consume(UserDTO userDTO);

    void userPictureUrlUpdate(UserDTO userDTO);
}
