CREATE TABLE users (
        id BIGINT(50) NOT NULL AUTO_INCREMENT,
        login CHAR(30) NOT NULL,
        password_hash CHAR(200) NOT NULL,
        PRIMARY KEY(id),
        UNIQUE (login),
        INDEX (login),
        INDEX user_find_index (login,password_hash)
);

CREATE TABLE fighters (
        id BIGINT(50) NOT NULL AUTO_INCREMENT,
        user_id BIGINT(50) NOT NULL,
        rating INT(10) NOT NULL,
        health INT(10) NOT NULL,
        damage INT(10) NOT NULL,
        PRIMARY KEY(id),
        UNIQUE (user_id),
        INDEX fighter_rating (rating),
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE duels (
        id BIGINT(50) NOT NULL AUTO_INCREMENT,
        first_user_id BIGINT(50) NOT NULL,
        second_user_id BIGINT(50) NOT NULL,
        status CHAR(20),
        create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        log TEXT,
        PRIMARY KEY(id),
        INDEX first_user_index (first_user_id),
        INDEX second_user_index (second_user_id),
        INDEX status_index (status),
        INDEX create_date_index (create_date)
);


