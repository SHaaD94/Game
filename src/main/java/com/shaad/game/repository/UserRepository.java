package com.shaad.game.repository;

import com.shaad.game.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
             PreparedStatement statement = connection.prepareStatement("SELECT id from users where login = ? and password_hash = ?")) {

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

    public boolean userExists(String login) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id from users where login = ?")) {

            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long saveUser(String login, String passwordHash) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (login,password_hash) values(?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, login);
            statement.setString(2, passwordHash);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                return resultSet.next() ? resultSet.getLong(1) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
