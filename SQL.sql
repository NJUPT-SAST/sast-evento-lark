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
    'cancelled'              BOOLEAN,
    PRIMARY KEY (`id`)
);
