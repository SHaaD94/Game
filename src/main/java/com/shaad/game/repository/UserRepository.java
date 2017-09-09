package com.shaad.game.repository;

import com.shaad.game.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
public class UserRepository extends Repository {
    public User getUserById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id,login,password_hash from users where id = ?")) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? new User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3))
                        : null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserByLoginAndPassword(String login, String passwordHash) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id from users where login = ? and password = ?")) {

            statement.setString(1, login);
            statement.setString(2, passwordHash);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? new User(
                        resultSet.getLong(1),
                        login,
                        passwordHash)
                        : null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //fixme handle duplicate login exception
    public void saveUser(String login, String passwordHash) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (login,password_hash) values(?,?)")) {

            statement.setString(1, login);
            statement.setString(2, passwordHash);
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
