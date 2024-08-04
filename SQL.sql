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
    `lark_meeting_room_id`   VARCHAR(255),
    `lark_department_id`     VARCHAR(255),
    PRIMARY KEY (`id`)
);
