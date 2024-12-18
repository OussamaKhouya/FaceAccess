create table Users
(
    id            INTEGER
        primary key autoincrement,
    firstName     TEXT              not null,
    lastName      TEXT,
    access        INT     default 0 not null,
    door          TEXT    default NULL,
    registredDate INTEGER default 0,
    sex           TEXT    default NULL
);

create table Admin
(
    id       INTEGER
        primary key autoincrement,
    username TEXT not null,
    email    TEXT not null
        unique,
    password TEXT not null
);

create table AccessLog
(
    id            INTEGER
        primary key autoincrement,
    userId        INTEGER
        references Users,
    timestamp     TEXT default CURRENT_TIMESTAMP,
    accessGranted INTEGER not null
);

INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (17, 'John', 'Doe', 1, 'A101', 20230101, 'Male');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (18, 'Jane', 'Smith', 0, 'B202', 20230215, 'Female');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (19, 'Michael', 'Johnson', 1, 'C303', 20220120, 'Male');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (20, 'Emily', 'Davis', 1, 'D404', 20231010, 'Female');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (21, 'Chris', 'Brown', 0, 'E505', 20221105, 'Male');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (22, 'Sophia', 'Taylor', 1, 'F606', 20211212, 'Female');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (23, 'Daniel', 'Wilson', 1, 'G707', 20230530, 'Male');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (24, 'Olivia', 'Moore', 0, 'H808', 20230808, 'Female');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (25, 'Ethan', 'Thomas', 1, 'I909', 20210621, 'Male');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (26, 'Isabella', 'Harris', 0, 'J010', 20231115, 'Female');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (27, 'Isabella', 'Harris', 0, 'J010', 20231115, 'Female');
INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (28, 'Emily', 'Davis', 1, 'D404', 20231010, 'Female');


INSERT INTO Admin (id, username, email, password) VALUES (11, 'admin', 'admin@gmail.com', '123');

INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (1, 18, '2024-12-15 23:54:08', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (7, 24, '2024-12-15 23:55:42', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (8, 25, '2024-12-15 23:55:42', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (9, 26, '2024-12-15 23:55:58', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (10, 27, '2024-12-15 23:55:58', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (11, 19, '2024-12-16 00:01:02', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (12, 20, '2024-12-16 00:03:15', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (13, 21, '2024-12-16 00:04:22', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (14, 22, '2024-12-16 00:05:45', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (15, 23, '2024-12-16 00:06:32', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (16, 24, '2024-12-16 12:15:10', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (17, 25, '2024-12-16 12:17:42', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (18, 26, '2024-12-16 13:25:33', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (19, 27, '2024-12-16 13:30:21', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (20, 28, '2024-12-16 14:45:10', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (21, 29, '2024-12-17 09:15:08', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (22, 30, '2024-12-17 09:20:18', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (23, 31, '2024-12-17 09:22:45', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (24, 32, '2024-12-17 10:14:30', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (25, 33, '2024-12-17 10:45:10', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (26, 34, '2024-12-17 11:01:02', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (27, 35, '2024-12-18 08:15:45', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (28, 36, '2024-12-18 08:20:50', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (29, 37, '2024-12-18 08:25:10', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (30, 38, '2024-12-18 08:30:30', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (31, 39, '2024-12-18 08:35:00', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (32, 40, '2024-12-18 08:40:20', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (33, 41, '2024-12-18 08:45:30', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (34, 42, '2024-12-18 08:50:40', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (35, 43, '2024-12-18 09:00:10', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (36, 44, '2024-12-18 09:05:20', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (37, 45, '2024-12-18 09:10:30', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (38, 46, '2024-12-18 09:15:40', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (39, 47, '2024-12-18 09:20:50', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (40, 48, '2024-12-18 09:25:10', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (41, 49, '2024-12-18 09:30:20', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (42, 50, '2024-12-18 09:35:30', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (43, 51, '2024-12-18 09:40:40', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (44, 52, '2024-12-18 09:45:50', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (45, 53, '2024-12-18 09:50:10', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (46, 54, '2024-12-18 09:55:20', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (47, 55, '2024-12-18 10:00:30', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (48, 56, '2024-12-18 10:05:40', 1);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (49, 57, '2024-12-18 10:10:50', 0);
INSERT INTO AccessLog (id, userId, timestamp, accessGranted) VALUES (50, 58, '2024-12-18 10:15:00', 1);
