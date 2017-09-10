package com.shaad.game.service;

import com.shaad.game.domain.User;
import com.shaad.game.exception.WrongUserPasswordException;
import com.shaad.game.repository.UserRepository;

import static com.shaad.game.util.SHA512.hash;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(String login, String password) {
        return userRepository.getUserByLoginAndPassword(login, hash(password));
    }

    public User findUser(Long id) {
        return userRepository.getUserById(id);
    }

    public Long createOrGetUser(String login, String password) {
        if (!userRepository.userExists(login)) {
            return userRepository.saveUser(login, password);
        }
        User user = userRepository.getUserByLoginAndPassword(login, password);
        if (user == null) {
            throw new WrongUserPasswordException();
        }
        return user.getId();
    }
}
