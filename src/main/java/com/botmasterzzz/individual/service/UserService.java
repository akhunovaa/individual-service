package com.botmasterzzz.individual.service;

import com.botmasterzzz.individual.dto.PasswordDTO;
import com.botmasterzzz.individual.dto.UserDTO;

public interface UserService {

    UserDTO save(UserDTO userDTO);

    void send(UserDTO userDTO);

    void consume(UserDTO userDTO);

    void userPictureUrlUpdate(UserDTO userDTO);

    void userPasswordUpdate(UserDTO userDTO);

    void userPasswordUpdate(PasswordDTO passwordDTO);

    UserDTO findUser(Long id);
}
