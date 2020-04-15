package com.botmasterzzz.individual.repository;

import com.botmasterzzz.individual.entity.UserApplicationSecretEntity;

import java.util.List;

public interface UserApplicationSecretDao {

    void userApplicationSecretSave(UserApplicationSecretEntity userApplicationSecretEntity);

    List<UserApplicationSecretEntity> userApplicationSecretList(Long userId, int limit);

}
