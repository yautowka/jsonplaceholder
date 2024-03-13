create table users
(
    id            int                                                    not null AUTO_INCREMENT,
    login         varchar(255)                                           not null,
    password_hash varchar(255)                                           not null,  
    role          ENUM ('ROLE_USERS_VIEWER', 'ROLE_ALBUMS_VIEWER', 'ROLE_ADMIN', 'ROLE_POSTS_VIEWER'
        ,'ROLE_USERS_EDITOR', 'ROLE_ALBUMS_EDITOR', 'ROLE_POSTS_EDITOR') not null,
    PRIMARY KEY (id)
);
create table audits
(
    id       int       not null AUTO_INCREMENT,
    time     timestamp not null,
    access   bool      not null,
    url      varchar(255),
    method   varchar(255),
    username varchar(255),
    params   varchar(255),
    PRIMARY KEY (id)
);
create table geos
(
    id  int   not null AUTO_INCREMENT,
    lat float not null,
    lng float not null,
    PRIMARY KEY (id)
);
create table addresses
(
    id      int not null AUTO_INCREMENT,
    street  varchar(255),
    suite   varchar(255),
    city    varchar(255),
    geo     int not null,
    zipcode varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (geo) REFERENCES geos (id)
);
create table companies
(
    id          int not null AUTO_INCREMENT,
    name        varchar(255),
    catchPhrase varchar(255),
    bs          varchar(255),
    PRIMARY KEY (id)
);
create table users_from_server
(
    id       int not null,
    name     varchar(255),
    username varchar(255),
    email    varchar(255),
    phone    varchar(255),
    address  int not null,
    website  varchar(255),
    company  int not null,
    PRIMARY KEY (id),
    FOREIGN KEY (address) REFERENCES addresses (id),
    FOREIGN KEY (company) REFERENCES companies (id)
);
create table albums
(
    userId int not null,
    id     int not null,
    title  varchar(255),
    PRIMARY KEY (id)
);
create table photos
(
    albumId      int not null,
    id           int not null,
    title        varchar(255),
    url          varchar(255),
    thumbnailUrl varchar(255),
    PRIMARY KEY (id)
);
create table posts
(
    userId int not null,
    id     int not null,
    title  varchar(255),
    body   varchar(255),
    PRIMARY KEY (id)
);
create table todos
(
    userId    int not null,
    id        int not null,
    title     varchar(255),
    completed bool,
    PRIMARY KEY (id)
);
create table comments
(
    id     int not null,
    postId int not null,
    name   varchar(255),
    email  varchar(255),
    body   varchar(255),
    PRIMARY KEY (id)
);

