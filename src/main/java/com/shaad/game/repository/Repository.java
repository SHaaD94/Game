package com.shaad.game.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;

import javax.sql.DataSource;

public abstract class Repository {
    protected static final DataSource dataSource;

    static {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersion(MigrationVersion.fromVersion("0.0.0"));
        flyway.setDataSource("jdbc:mysql://127.0.0.1:3306/game?useSSL=false",
                "root", "password");
        flyway.migrate();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/game?useSSL=false");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("password");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setPoolName("Game");
        hikariConfig.setCatalog("game");

        dataSource = new HikariDataSource(hikariConfig);
    }
}
