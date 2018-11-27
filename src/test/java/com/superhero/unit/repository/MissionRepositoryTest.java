package com.superhero.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.superhero.model.Mission;
import com.superhero.repository.MissionRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
//@SpringBootTest
public class MissionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MissionRepository missionRepository;
    
    @Test
    public void softDeleteById() {
        //given
        Mission mission = new Mission();
        mission.setName("Mission05");
        mission.setCompleted(true);
        entityManager.persist(mission);
        entityManager.flush();

        //when
        missionRepository.softDeleteById(mission.getId());

        Optional<Mission> found = missionRepository.findById(mission.getId());

        //then
        assertThat(found.empty());
        
        //else when
        List<Mission> allDeleted = missionRepository.findAllDeleted();

        //then
        assertThat(allDeleted.contains(mission));
    }
   

    @Test
    public void findByIdAndNotSoftDeleted() {
        //given
        Mission mission = new Mission();
        mission.setName("Mission10");
        mission.setCompleted(true);
        entityManager.persist(mission);
        entityManager.flush();

        //when
        Optional<Mission> found = missionRepository.findById(mission.getId());

        //then
        assertThat(found.isPresent() && mission.getName().equals(found.get().getName()) && mission.isCompleted() == found.get().isCompleted());
    }
    
    @Test
    public void findByIdAndSoftDeleted() {
        //given
        Mission mission = new Mission();
        mission.setName("Mission11");
        mission.setCompleted(true);
        mission.setDeleted(true);
        entityManager.persist(mission);
        entityManager.flush();

        //when
        Optional<Mission> found = missionRepository.findById(mission.getId());

        //then
        assertThat(found.empty());
        
        //else when
        List<Mission> allDeleted = missionRepository.findAllDeleted();

        //then
        assertThat(allDeleted.contains(mission));
    }
    
    @Test
    public void findByNameAndNotSoftDeleted() {
        //given
        Mission mission = new Mission();
        mission.setName("Mission14");
        mission.setCompleted(true);
        entityManager.persist(mission);
        entityManager.flush();

        //when
        //Optional<Mission> found = missionRepository.findByName(mission.getName().toLowerCase());
        List<Mission> allByName = missionRepository.findByName(mission.getName().toLowerCase());

        //then
        //assertThat(found.isPresent() && mission.getName().equals(found.get().getName()) && mission.isCompleted() == found.get().isCompleted());
        assertThat(allByName.size() == 1);
        assertThat(mission.getName().equals(allByName.get(0).getName()) && mission.isCompleted() == allByName.get(0).isCompleted());
    }
    
    @Test
    public void findByNameAndSoftDeleted() {
        //given
        Mission mission = new Mission();
        mission.setName("Mission15");
        mission.setCompleted(true);
        mission.setDeleted(true);
        entityManager.persist(mission);
        entityManager.flush();

        //when
        //Optional<Mission> found = missionRepository.findByName(mission.getName().toLowerCase());
        List<Mission> allByName = missionRepository.findByName(mission.getName().toLowerCase());

        //then
        //assertThat(found.empty());
        assertThat(allByName.isEmpty());
        
        //else when
        List<Mission> allDeleted = missionRepository.findAllDeleted();

        //then
        assertThat(allDeleted.contains(mission));
    }
    
    @Test
    public void findAll() {
        //given
        Mission mission20 = new Mission();
        mission20.setName("Mission20");
        mission20.setCompleted(true);
        entityManager.persist(mission20);
        entityManager.flush();
        
        Mission mission21 = new Mission();
        mission21.setName("Mission21");
        mission21.setCompleted(true);
        mission21.setDeleted(true);
        entityManager.persist(mission21);
        entityManager.flush();
    	
        //when
        List<Mission> all = missionRepository.findAll();

        //then
        assertThat(all.contains(mission20));
        assertThat(!all.contains(mission21));
    }
    
    @Test
    public void findAllCompleted() {
        //given
        Mission mission40 = new Mission();
        mission40.setName("Mission40");
        mission40.setCompleted(true);
        entityManager.persist(mission40);
        entityManager.flush();
        
        Mission mission41 = new Mission();
        mission41.setName("Mission41");
        mission41.setCompleted(false);
        entityManager.persist(mission41);
        entityManager.flush();
    	
        //when
        List<Mission> allCompleted = missionRepository.findAllCompleted();

        //then
        assertThat(allCompleted.size() > 2);
        assertThat(allCompleted.contains(mission40));
        assertThat(!allCompleted.contains(mission41));
    }
}
