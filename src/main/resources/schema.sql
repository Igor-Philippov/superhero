drop database if exists super_hero_company;

create database if not exists super_hero_company;

use super_hero_company;

select 'CREATING DATABASE STRUCTURE' as 'INFO';

drop table if exists shc_super_hero_mission,
                     shc_mission,
                     shc_super_hero;

create table shc_super_hero (
    id             bigint       not null auto_increment,
    name_first     varchar(32)  not null,
    name_last      varchar(64)  not null,
    name_superhero varchar(128) character set latin1 collate latin1_general_ci not null,
	unique key IDX1_super_hero (name_superhero),
    primary key (id)
);

create table shc_mission (
    id           bigint       not null auto_increment,
    name         varchar(255) character set latin1 collate latin1_general_ci not null,
    is_completed tinyint(1)   not null default 0,
    is_deleted   tinyint(1)   not null default 0,
    unique key IDX1_shc_super_hero (name),
    primary key (id)
);

create table shc_super_hero_mission (
    sh_id        bigint     not null,
    m_id         bigint     not null,
    foreign key (sh_id) references shc_super_hero(id) on delete cascade,
    foreign key (m_id) references shc_mission(id) on delete restrict,
	unique key IDX1_shc_super_hero_mission (m_id, sh_id),
    primary key (sh_id, m_id)
); 

create user if not exists 'shc_user'@'localhost' identified by 'password';

grant all on super_hero_company.* to 'shc_user'@'localhost';
