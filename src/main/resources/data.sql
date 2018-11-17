insert into shc_super_hero (id, name_first, name_last, name_superhero) values (1, 'John', 'Smith', 'john.smith@shc.com');
insert into shc_super_hero (id, name_first, name_last, name_superhero) values (5, 'Igor', 'Philippov', 'igor.philippov@shc.com');
insert into shc_super_hero (id, name_first, name_last, name_superhero) values (7, 'James', 'Bond', 'james.bond@shc.com');
insert into shc_super_hero (id, name_first, name_last, name_superhero) values (8, 'James', 'Brown', 'james.brown@shc.com');
insert into shc_super_hero (id, name_first, name_last, name_superhero) values (15, 'Fomba', 'Mamadou', 'fomba.mamadou@shc.com');
insert into shc_super_hero (id, name_first, name_last, name_superhero) values (17, 'Jean-Luc', 'Dupuis', 'jean-luc.dupuis@shc.com');
insert into shc_super_hero (id, name_first, name_last, name_superhero) values (18, 'Luise', 'Dupuis', 'luise.dupuis@shc.com');

insert into shc_mission (id, name, is_completed, is_deleted) values (100, 'EAT', 0, 1);
insert into shc_mission (id, name) values (110, 'SLEEP');
insert into shc_mission (id, name) values (120, 'WORK');
insert into shc_mission (id, name) values (130, 'TRAVEL');
insert into shc_mission (id, name, is_completed) values (150, 'REST', 1);

-- luise.dupuis@shc.com
insert into shc_super_hero_mission (sh_id, m_id) values (18, 130);

-- john.smith@shc.com
insert into shc_super_hero_mission (sh_id, m_id) values (1, 100);
insert into shc_super_hero_mission (sh_id, m_id) values (1, 110);
insert into shc_super_hero_mission (sh_id, m_id) values (1, 120);
insert into shc_super_hero_mission (sh_id, m_id) values (1, 130);

-- james.brown@shc.com
insert into shc_super_hero_mission (sh_id, m_id) values (8, 100);
insert into shc_super_hero_mission (sh_id, m_id) values (8, 110);
insert into shc_super_hero_mission (sh_id, m_id) values (8, 120);
insert into shc_super_hero_mission (sh_id, m_id) values (8, 130);

-- igor.philippov@sch.com
insert into shc_super_hero_mission (sh_id, m_id) values (5, 100);
insert into shc_super_hero_mission (sh_id, m_id) values (5, 110);
insert into shc_super_hero_mission (sh_id, m_id) values (5, 120);
insert into shc_super_hero_mission (sh_id, m_id) values (5, 150);
