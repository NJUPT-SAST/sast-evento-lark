CREATE TABLE `attachment`
(
    `id`       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT UNSIGNED NOT NULL,
    `url`      VARCHAR(255)    NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `event`
(
    `id`                     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `summary`                VARCHAR(255)    NOT NULL,
    `description`            TEXT,
    `start`                  DATETIME,
    `end`                    DATETIME,
    `location`               VARCHAR(255),
    `tag`                    VARCHAR(255),
    `lark_event_uid`         VARCHAR(64),
    `lark_meeting_room_name` VARCHAR(255),
    `lark_department_name`   VARCHAR(255),
    `cancelled`              BOOLEAN,
    PRIMARY KEY (`id`)
);
CREATE TABLE `feedback`
(
    `id`       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT UNSIGNED NOT NULL,
    `link_id`  VARCHAR(16)     NOT NULL,
    `rating`   INT             NOT NULL,
    `content`  TEXT,
    PRIMARY KEY (`id`)
);
CREATE TABLE `subscription`
(
    `id`         BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `event_id`   BIGINT UNSIGNED       NOT NULL,
    `link_id`    VARCHAR(16)           NOT NULL,
    `subscribed` BOOLEAN DEFAULT FALSE NOT NULL,
    `checked_in` BOOLEAN DEFAULT FALSE NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `department_subscription`
(
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `link_id`       VARCHAR(16)     NOT NULL,
    `department_id` VARCHAR(64)     NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `slide`
(
    `id`       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT UNSIGNED NOT NULL,
    `oss_key`  VARCHAR(64)     NOT NULL,
    `link`     VARCHAR(255)    NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `message`
(
    `id`       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT UNSIGNED NOT NULL,
    `state`    VARCHAR(16)     NOT NULL,
    `time`     DATETIME        NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `permission`
(
    `user_id`    VARCHAR(16) NOT NULL,
    `permission` INT,
    PRIMARY KEY (`user_id`)
);
