create table equipment_user
(
    id         int auto_increment
        primary key,
    username   varchar(15)       null,
    account    varchar(20)       null,
    password   varchar(100)      null,
    photo      varchar(100)      null,
    level      tinyint default 0 not null,
    addTime    datetime          null,
    modifyTime datetime          null,
    status     tinyint default 0 null,
    email      varchar(20)       not null,
    constraint equipment_user_account_uindex
        unique (account)
);

create table equipment_form
(
    id             int auto_increment
        primary key,
    name           varchar(50)       null,
    category       tinyint           null,
    type           varchar(50)       null,
    specification  varchar(50)       null,
    price          double            null,
    number         int               null,
    buyDate        datetime          null,
    supportServer  varchar(100)      null,
    expirationDate datetime          null,
    applicant      varchar(30)       null,
    fixDate        datetime          null,
    fixServer      varchar(100)      null,
    sumPrice       double            null,
    position       varchar(500)      null,
    department     int               not null,
    reason         varchar(500)      null,
    note           varchar(500)      null,
    addTime        datetime          null,
    status         tinyint default 0 null,
    userId         int               not null,
    serial         varchar(50)       not null,
    formStatus     tinyint           not null,
    level          tinyint default 0 not null,
    formType       tinyint           not null,
    handlerId      int               null,
    modifyTime     datetime          not null
);

