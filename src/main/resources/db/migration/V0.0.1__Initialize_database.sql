CREATE TABLE `users` (
        `id` BIGINT(50) NOT NULL AUTO_INCREMENT,
        `login` CHAR(30) NOT NULL,
        `password_hash` CHAR(200) NOT NULL,
        PRIMARY KEY(`id`),
        UNIQUE (`login`),
        INDEX (`login`),
        INDEX user_find_index (`login`,`password_hash`)
);

CREATE TABLE `fighters` (
        `id` BIGINT(50) NOT NULL AUTO_INCREMENT,
        `user_id` BIGINT(50) NOT NULL,
        `rating` INT(10) NOT NULL,
        `health` INT(10) NOT NULL,
        `damage` INT(10) NOT NULL,
        PRIMARY KEY(`id`),
        INDEX fighter_rating (`rating`),
        FOREIGN KEY (`user_id`) REFERENCES users(`id`)
);

CREATE TABLE `fights` (
        `id` BIGINT(50) NOT NULL AUTO_INCREMENT,
        `first_fighter_id` BIGINT(50) NOT NULL,
        `second_fighter_id` BIGINT(50) NOT NULL,
        `log` TEXT NOT NULL,
        `winner` INT(1),
        PRIMARY KEY(`id`)
);


