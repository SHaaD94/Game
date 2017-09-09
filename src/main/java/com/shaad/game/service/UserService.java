package com.shaad.game.service;

import com.shaad.game.domain.User;
import com.shaad.game.repository.UserRepository;

import static com.shaad.game.util.SHA512.hash;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String login, String password) {
        userRepository.saveUser(login, hash(password));
    }

    public User findUser(String login, String password) {
        return userRepository.getUserByLoginAndPassword(login, hash(password));
    }
}
