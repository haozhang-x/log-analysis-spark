create database website_log;
use website_log;
#总PV
create table total_pv
(
    id       bigint auto_increment primary key,
    total_pv varchar(256) not null
);

# ip pv
create table ip_pv
(
    id bigint auto_increment primary key,
    ip varchar(100) not null,
    pv varchar(256) not null
);

# search engine pv
create table search_engine_pv
(
    id            bigint auto_increment primary key,
    search_engine varchar(100) not null,
    pv            varchar(256) not null
);

# keyword pv
create table keyword_pv
(
    id      bigint auto_increment primary key,
    keyword varchar(100) not null,
    pv      varchar(256) not null
);

#agent pv
create table agent_pv
(
    id    bigint auto_increment primary key,
    agent varchar(100) not null,
    pv    varchar(256) not null
);


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

delete from total_pv;
delete from keyword_pv;
delete from agent_pv;
delete from search_engine_pv;
delete from ip_pv;
