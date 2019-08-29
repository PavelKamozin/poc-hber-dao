ALTER TABLE users
    ADD COLUMN `last_name` VARCHAR(255) NULL AFTER `name`,
        ADD COLUMN `job` VARCHAR(255) NULL AFTER `last_name`;