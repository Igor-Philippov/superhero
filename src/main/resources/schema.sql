# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL '8.0.13')
# Database: super_hero_company
# Generation Time: 2018-11-18 22:11:21 +0000
# ************************************************************

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

drop database if exists `super_hero_company`;

create database if not exists `super_hero_company`;

use `super_hero_company`;

select 'CREATING DATABASE STRUCTURE' as 'INFO';

# ------------------------------------------------------------
# Dump of table shc_super_hero_mission
# ------------------------------------------------------------

drop table if exists `shc_super_hero_mission`;

# ------------------------------------------------------------
# Dump of table shc_super_hero
# ------------------------------------------------------------

drop table if exists `shc_super_hero`;

create table `shc_super_hero` (
    `id`             bigint       not null auto_increment,
    `name_first`     varchar(32)  not null,
    `name_last`      varchar(64)  not null,
    `name_superhero` varchar(128) character set latin1 collate latin1_general_ci not null,
    `is_deleted`     tinyint(1)   not null default 0, -- reserved for soft deletion if can be required
	unique key `IDX1_super_hero` (`name_superhero`),
    primary key (`id`)
) engine=InnoDB default CHARSET=latin1;

# ------------------------------------------------------------
# Dump of table shc_mission
# ------------------------------------------------------------

drop table if exists `shc_mission`;

create table `shc_mission` (
    `id`           bigint       not null auto_increment,
    `name`         varchar(255) character set latin1 collate latin1_general_ci not null,
    `is_completed` tinyint(1)   not null default 0,
    `is_deleted`   tinyint(1)   not null default 0, -- reserved for soft deletion
    -- unique key `IDX1_shc_super_hero` (`name`),
    primary key (`id`)
) engine=InnoDB default CHARSET=latin1;

# ------------------------------------------------------------
# Dump of table shc_super_hero_mission
# ------------------------------------------------------------

create table `shc_super_hero_mission` (
    `sh_id` bigint not null,
    `m_id`  bigint not null,
    foreign key (`sh_id`) references `shc_super_hero`(`id`) on delete cascade,
    foreign key (`m_id`) references `shc_mission`(`id`) on delete restrict,
	unique key `IDX1_shc_super_hero_mission` (`m_id`, `sh_id`),
    primary key (`sh_id`, `m_id`)
) engine=InnoDB default CHARSET=latin1;

# ------------------------------------------------------------
# Roles / users / priveleges
# ------------------------------------------------------------
create role if not exists 'ADMIN'@'localhost';

grant all on super_hero_company.* to 'ADMIN'@'localhost';

create user if not exists 'shc_user'@'localhost' identified by 'password';

grant 'ADMIN'@'localhost' to 'shc_user'@'localhost';
