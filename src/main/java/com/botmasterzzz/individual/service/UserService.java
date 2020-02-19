package com.botmasterzzz.individual.service;

import com.botmasterzzz.individual.dto.IndividualDTO;
import com.botmasterzzz.individual.dto.PasswordDTO;
import com.botmasterzzz.individual.dto.UserDTO;

public interface UserService {

    UserDTO save(UserDTO userDTO);

    void send(UserDTO userDTO);

    void consume(UserDTO userDTO);

    void userPictureUrlUpdate(UserDTO userDTO);

    void userPasswordUpdate(UserDTO userDTO);

    void userPasswordUpdate(PasswordDTO passwordDTO);

    void individualUpdate(IndividualDTO individualDTO);

    UserDTO findUser(Long id);

    IndividualDTO findIndividual(Long id);
}
