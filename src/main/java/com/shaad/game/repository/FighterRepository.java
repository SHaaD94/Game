package com.shaad.game.repository;

import com.shaad.game.domain.Fighter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FighterRepository extends Repository {
    public void createUsersFighter(long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO fighters (user_id, rating, health, damage) " +
                             "values(?,0,100,10)")) {

            preparedStatement.setLong(1, userId);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Fighter getUserFighter(Long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * from fighters id, rating, health, damage where user_id=?")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? new Fighter(
                        resultSet.getLong(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
