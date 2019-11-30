package com.botmasterzzz.individual.repository;

import com.botmasterzzz.individual.entity.User;

import java.util.Optional;

public interface UserDao {

        Optional<User> findById(Long id);

        Boolean existsByLogin(String login);

        Boolean existsByEmail(String email);

}
