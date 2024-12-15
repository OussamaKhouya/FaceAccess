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
