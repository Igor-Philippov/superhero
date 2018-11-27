# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL '8.0.13')
# Database: test_super_hero_company
# Generation Time: 2018-11-18 22:11:21 +0000
# ************************************************************

use `super_hero_company`;

# ------------------------------------------------------------
# Data for [shc_super_hero_mission]
# ------------------------------------------------------------
lock tables `shc_super_hero` write;
/*!40000 alter table `shc_super_hero` disable keys */;

insert into `shc_super_hero` (`id`, `name_first`, `name_last`, `name_superhero`)
values (1, 'John', 'Smith', 'john.smith@shc.com'),
       (5, 'Igor', 'Philippov', 'igor.philippov@shc.com'),
       (7, 'James', 'Bond', 'james.bond@shc.com'),
       (8, 'James', 'Brown', 'james.brown@shc.com'),
       (15, 'Fomba', 'Mamadou', 'fomba.mamadou@shc.com'),
       (17, 'Jean-Luc', 'Dupuis', 'jean-luc.dupuis@shc.com'),
       (18, 'Luise', 'Dupuis', 'luise.dupuis@shc.com');

/*!40000 alter table `shc_super_hero` enable keys */;
unlock tables;

# ------------------------------------------------------------
# Data for [shc_mission]
# ------------------------------------------------------------
lock tables `shc_mission` write;
/*!40000 alter table `shc_mission` disable keys */;

insert into `shc_mission` (`id`, `name`, `is_completed`, `is_deleted`)
values (90, 'UNCOMPLETED_UNDELETED', 0, 0),
       (91, 'COMPLETED_UNDELETED', 1, 0),
       (92, 'UNCOMPLETED_DELETED', 0, 1),
       (93, 'COMPLETED_DELETED', 1, 1),
       (100, 'EAT', 0, 1);
	   
insert into `shc_mission` (`id`, `name`)
values (110, 'SLEEP'),
       (120, 'WORK'),
       (130, 'TRAVEL');
	   
insert into `shc_mission` (`id`, `name`, `is_completed`)
values (150, 'REST', 1);

/*!40000 alter table `shc_mission` enable keys */;
unlock tables;

# ------------------------------------------------------------
# Data for [shc_super_hero_mission]
# ------------------------------------------------------------
lock tables `shc_super_hero_mission` write;
/*!40000 alter table `shc_super_hero_mission` disable keys */;

# luise.dupuis@shc.com
insert into `shc_super_hero_mission` (`sh_id`, `m_id`) values (18, 130);

# john.smith@shc.com
insert into `shc_super_hero_mission` (`sh_id`, `m_id`)
values (1, 100),
       (1, 110),
       (1, 120),
       (1, 130);
	   
# james.brown@shc.com
insert into `shc_super_hero_mission` (`sh_id`, `m_id`)
values (8, 100),
       (8, 110),
       (8, 120),
       (8, 130);

# igor.philippov@sch.com
insert into `shc_super_hero_mission` (`sh_id`, `m_id`)
values (5, 100),
       (5, 110),
       (5, 120),
       (5, 150);

/*!40000 alter table `shc_super_hero_mission` enable keys */;
unlock tables;
