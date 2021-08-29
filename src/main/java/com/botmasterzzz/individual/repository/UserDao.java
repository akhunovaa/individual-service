package com.botmasterzzz.individual.repository;

import com.botmasterzzz.individual.entity.Individual;
import com.botmasterzzz.individual.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(Long id);

    Optional<Individual> findIndividualById(Long id);

    Optional<Individual> findIndividualByNickName(String nickname);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

    void userUpdate(User user);

    void individualUpdate(Individual individual);

}
