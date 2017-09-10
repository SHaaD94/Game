package com.shaad.game.repository;

import com.shaad.game.domain.Duel;
import com.shaad.game.domain.DuelStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;

public class DuelRepository extends Repository {
    public Duel getLastFinishedDuel(long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM duels WHERE first_user_id = ? or second_user_id = ? " +
                             "AND status='FINISHED'" +
                             "ORDER BY create_date DESC limit 1")) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                long id = resultSet.getLong("id");
                long firstUserId = resultSet.getLong("first_user_id");
                long secondUserId = resultSet.getLong("second_user_id");
                Date createDate = new Date(resultSet.getTimestamp("create_date").getTime());
                DuelStatus status = DuelStatus.valueOf(resultSet.getString("status"));
                Duel duel = new Duel(id, userId, firstUserId != userId ? firstUserId : secondUserId, createDate, status);
                Arrays.stream(resultSet.getString("log").split("\n")).forEachOrdered(duel::addLogLine);
                return duel;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Duel createDuel(long firstUserId, long secondUserId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO duels (first_user_id, second_user_id, status) " +
                             "values(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, firstUserId);
            preparedStatement.setLong(2, secondUserId);
            preparedStatement.setString(3, DuelStatus.PENDING.toString());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                long id = resultSet.getLong(1);
                //fixme: hack around mysql returning generated date
                return new Duel(id, firstUserId, secondUserId, new Date(), DuelStatus.PENDING);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDuelStatus(Duel duel) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE duels set status = ? where id = ?")) {

            preparedStatement.setString(1, duel.getStatus().toString());
            preparedStatement.setLong(2, duel.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDuelLogs(Duel duel) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE duels set log = ? where id = ?")) {

            preparedStatement.setString(1, duel.aggregateLogs());
            preparedStatement.setLong(2, duel.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDuel(Duel duel) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE duels set status = ?, log = ? where id = ?")) {

            preparedStatement.setString(1, duel.getStatus().toString());
            preparedStatement.setString(2, duel.aggregateLogs());
            preparedStatement.setLong(3, duel.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
