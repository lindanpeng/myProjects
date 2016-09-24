create database chat character set gbk;
use chat;
create table User(
userid varchar(20) primary key,
password varchar(20)
);
create table GroupMessage(
id int auto_increment primary key,
content text,
time varchar(20) not null default '',
userid varchar(20) not null default ''
);
create table FriendMessage(
id int auto_increment primary key,
userid varchar(20) not null default '',
friendid varchar(20) not null default '',
content text,
time varchar(20) not null default ''
);
create table Friend(
id int auto_increment primary key,
friendid varchar(20) not null default '',
userid varchar(20) not null default ''
);