package com.shaad.game.service;

import com.shaad.game.domain.User;
import com.shaad.game.exception.WrongUserPasswordException;
import com.shaad.game.repository.FighterRepository;
import com.shaad.game.repository.UserRepository;

import static com.shaad.game.util.SHA512.hash;

public class UserService {
    private final UserRepository userRepository;
    private final FighterRepository fighterRepository;

    public UserService(UserRepository userRepository, FighterRepository fighterRepository) {
        this.userRepository = userRepository;
        this.fighterRepository = fighterRepository;
    }

    public User findUser(String login, String password) {
        return userRepository.getUserByLoginAndPassword(login, hash(password));
    }

    public User findUser(Long id) {
        return userRepository.getUserById(id);
    }

    public Long createOrGetUser(String login, String password) {
        if (!userRepository.userExists(login)) {
            Long userId = userRepository.saveUser(login, password);
            fighterRepository.createUsersFighter(userId);
            return userId;
        }
        User user = userRepository.getUserByLoginAndPassword(login, password);
        if (user == null) {
            throw new WrongUserPasswordException();
        }
        return user.getId();
    }
}
