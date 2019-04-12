create database website_log;
use website_log;
#总PV
create table total_pv
(
    id       bigint auto_increment primary key,
    total_pv varchar(256) not null
);


alter table total_pv
    add column create_time timestamp default current_timestamp,
    add column update_time timestamp default current_timestamp on update current_timestamp;


# ip pv
create table ip_pv
(
    id bigint auto_increment primary key,
    ip varchar(100) not null,
    pv varchar(256) not null
);
alter table ip_pv
    add column create_time timestamp default current_timestamp,
    add column update_time timestamp default current_timestamp on update current_timestamp;

# search engine pv
create table search_engine_pv
(
    id            bigint auto_increment primary key,
    search_engine varchar(100) not null,
    pv            varchar(256) not null
);

alter table search_engine_pv
    add column create_time timestamp default current_timestamp,
    add column update_time timestamp default current_timestamp on update current_timestamp;

# keyword pv
create table keyword_pv
(
    id      bigint auto_increment primary key,
    keyword varchar(100) not null,
    pv      varchar(256) not null
);


alter table keyword_pv
    add column create_time timestamp default current_timestamp,
    add column update_time timestamp default current_timestamp on update current_timestamp;
#agent pv
create table agent_pv
(
    id    bigint auto_increment primary key,
    agent varchar(100) not null,
    pv    varchar(256) not null
);
alter table agent_pv
    add column create_time timestamp default current_timestamp,
    add column update_time timestamp default current_timestamp on update current_timestamp;


select *
from total_pv;


select *
from keyword_pv;


select *
from agent_pv;

select *
from search_engine_pv;

select *
from ip_pv;

#clear data
delete
from total_pv;
delete
from keyword_pv;
delete
from agent_pv;
delete
from search_engine_pv;
delete
from ip_pv;
