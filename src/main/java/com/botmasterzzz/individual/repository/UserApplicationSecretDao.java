package com.botmasterzzz.individual.repository;

import com.botmasterzzz.individual.entity.UserApplicationSecretEntity;

import java.util.List;
import java.util.Optional;

public interface UserApplicationSecretDao {

    void userApplicationSecretSave(UserApplicationSecretEntity userApplicationSecretEntity);

    void userApplicationSecretUpdate(UserApplicationSecretEntity userApplicationSecretEntity);

    Optional<UserApplicationSecretEntity> userApplicationSecretGet(Long userId, String name);

    List<UserApplicationSecretEntity> userApplicationSecretList(Long userId, int limit);

}
