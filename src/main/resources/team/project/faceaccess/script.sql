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