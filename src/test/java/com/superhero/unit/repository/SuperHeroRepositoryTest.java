package com.superhero.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.superhero.model.Mission;
import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class SuperHeroRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuperHeroRepository superheroRepository;

    private SuperHero superHero;

    @Before
    public void setUp() {
    	superHero = new SuperHero("firstName", "lastName", "superHeroName");
    }

    @Test
    public void findAllSuperHeros() throws Exception {
        //given
        entityManager.persist(superHero);
        entityManager.flush();

        //when
        List<SuperHero> all = superheroRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(8);
        assertThat(all.get(7)).isEqualTo(superHero);
    }

    @Test
    public void findSuperHeroById() throws Exception {
        //given
        entityManager.persist(superHero);
        entityManager.flush();

        //when
        Optional<SuperHero> found = superheroRepository.findById(superHero.getId());

        //then
        assertThat(superHero.getFirstName()).isEqualTo(found.get().getFirstName());
        assertThat(superHero.getLastName()).isEqualTo(found.get().getLastName());
        assertThat(superHero.getSuperHeroName()).isEqualTo(found.get().getSuperHeroName());
    }
    
    @Test
    public void findSuperHeroBySuperHeroName() {
        //given
    	SuperHero superHero = new SuperHero("firstName10", "lastName10", "superHeroName10");
    	Mission mission = new Mission();
    	mission.setName("MISSION_FOR_superHeroName10");
        mission.setCompleted(true);
        entityManager.persist(mission);
        entityManager.flush();
        
        superHero.addMission(mission);
        entityManager.persist(superHero);
        entityManager.flush();

        //when
        Optional<SuperHero> found = superheroRepository.findBySuperHeroName(superHero.getSuperHeroName());

        //then
        assertThat(found.isPresent() && superHero.getSuperHeroName().equals(found.get().getSuperHeroName()));
        assertThat(found.get().getMissions().size() == 1);
        assertThat(found.get().getMissions().contains(mission));
    }
    
    @Test
    public void findSuperHerosByFirstName() {
        //given
    	Mission mission = new Mission();
    	mission.setName("MISSION_FOR_NameX");
        entityManager.persist(mission);
        entityManager.flush();
    	
    	SuperHero superHeroA1 = new SuperHero("firstNameA", "lastNameA", "superHeroNameA1");
    	superHeroA1.addMission(mission);
    	
    	SuperHero superHeroA2 = new SuperHero("FIRSTNameA", "LAStNameA", "superHeroNameA2");
    	superHeroA2.addMission(mission);
    	
        entityManager.persistAndFlush(superHeroA1);
        entityManager.persistAndFlush(superHeroA2);

        //when
        List<SuperHero> all = superheroRepository.findByFirstName(superHeroA2.getFirstName().toUpperCase());

        //then
        assertThat(all.size() == 2);
        assertThat(all.contains(superHeroA1));
        assertThat(all.contains(superHeroA2));
    }
    
    @Test
    public void findSuperHerosByLastName() {
        //given   	
    	SuperHero superHeroB1 = new SuperHero("firstNameB1", "lastNameB", "superHeroNameB1");
    	SuperHero superHeroB2 = new SuperHero("firstNameB2", "LASTNAMEb", "superHeroNameB2");
        entityManager.persistAndFlush(superHeroB1);
        entityManager.persistAndFlush(superHeroB1);

        //when
        List<SuperHero> all = superheroRepository.findByLastName(superHeroB1.getLastName().toUpperCase());

        //then
        assertThat(all.size() == 2);
        assertThat(all.contains(superHeroB1));
        assertThat(all.contains(superHeroB2));
    }

    @Test
    public void deleteById() throws Exception {
        //given
        entityManager.persist(superHero);
        entityManager.flush();

        //when
        superheroRepository.deleteById(superHero.getId());
        List<SuperHero> all = superheroRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(7);
    }

}