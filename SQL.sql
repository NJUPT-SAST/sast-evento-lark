CREATE TABLE `attachment`
(
    `id`       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT(20)          NOT NULL,
    `url`      VARCHAR(255)        NOT NULL,
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
    `lark_event_uid`         VARCHAR(255),
    `lark_meeting_room_name` VARCHAR(255),
    `lark_department_name`   VARCHAR(255),
    `cancelled`              BOOLEAN,
    PRIMARY KEY (`id`)
);
CREATE TABLE `feedback`
(
    `id`       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT(20)          NOT NULL,
    `link_id`  VARCHAR(255)        NOT NULL,
    `rating`   INT,
    `content`  TEXT,
    PRIMARY KEY (`id`)
);
CREATE TABLE `subscription`
(
    `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id`   BIGINT(20)          NOT NULL,
    `link_id`    VARCHAR(255)        NOT NULL,
    `subscribed` BOOLEAN             NOT NULL,
    `checked_in`  BOOLEAN             NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `department_subscription`
(
    `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `link_id`       VARCHAR(255)        NOT NULL,
    `department_id` VARCHAR(20)         NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `slide`
(
    `id`       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT(20)          NOT NULL,
    `url`      VARCHAR(255)        NOT NULL,
    `link`     VARCHAR(255)        NOT NULL,
    PRIMARY KEY (`id`)
);