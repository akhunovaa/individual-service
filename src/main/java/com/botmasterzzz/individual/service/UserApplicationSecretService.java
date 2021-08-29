package com.botmasterzzz.individual.service;

import com.botmasterzzz.individual.dto.UserApplicationSecretDTO;

import java.util.List;

public interface UserApplicationSecretService {

    UserApplicationSecretDTO createNewUserApplicationSecret(Long userId, String secretName);

    List<UserApplicationSecretDTO> getUserApplicationSecretList(Long userId, int limit);

    void updateUserApplicationSecret(Long userId, String secretName, String updateName);

    void deleteUserApplicationSecret(Long userId, String secretName);

}
