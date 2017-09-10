package com.shaad.game.repository;

import com.shaad.game.domain.Duel;
import com.shaad.game.domain.DuelStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

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
                DuelStatus status = DuelStatus.valueOf(resultSet.getString("status"));
                Duel duel = new Duel(id, userId, firstUserId != userId ? firstUserId : secondUserId, status);
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
                     "INSERT INTO duels (first_user_id, second_user_id, status, create_date) " +
                             "values(?,?,?,now())", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, firstUserId);
            preparedStatement.setLong(2, secondUserId);
            preparedStatement.setString(3, DuelStatus.PENDING.toString());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                long id = resultSet.getLong(1);
                return new Duel(id, firstUserId, secondUserId, DuelStatus.PENDING);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDuelStatus(long duelId, DuelStatus status) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE duels set status = ? where id = ?")) {

            preparedStatement.setString(1, status.toString());
            preparedStatement.setLong(2, duelId);
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
