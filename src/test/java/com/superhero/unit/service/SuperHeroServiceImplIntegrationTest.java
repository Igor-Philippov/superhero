package com.superhero.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.superhero.model.SuperHero;
import com.superhero.repository.MissionRepository;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.service.SuperHeroService;
import com.superhero.service.impl.SuperHeroServiceImpl;

@RunWith(SpringRunner.class)
	public class SuperHeroServiceImplIntegrationTest {
	 
	    @TestConfiguration
	    static class SuperHeroServiceImplTestContextConfiguration {
	  
	        @Bean
	        public SuperHeroService superHeroService() {
	            return new SuperHeroServiceImpl();
	        }
	    }
	 
	    @Autowired
	    private SuperHeroService superHeroService;
	 
	    @MockBean
	    private SuperHeroRepository superHeroRepository;
	    
	    @MockBean
	    private MissionRepository missionRepository;
	 
	    @Before
	    public void setUp() {
	        SuperHero sh1 = new SuperHero("firstName1", "lastName1", "superHeroName1");
	        sh1.setId(-1L);
	        SuperHero sh2 = new SuperHero("firstName2", "lastName2", "superHeroName2");
	        SuperHero sh3 = new SuperHero("firstName3", "lastName3", "superHeroName3");

	        List<SuperHero> allSuperHeros = Arrays.asList(sh1, sh2, sh3);

	        Mockito.when(superHeroRepository.findBySuperHeroName(sh1.getSuperHeroName())).thenReturn(Optional.of(sh1));
	        Mockito.when(superHeroRepository.findBySuperHeroName(sh2.getSuperHeroName())).thenReturn(Optional.of(sh2));
	        Mockito.when(superHeroRepository.findBySuperHeroName("wrong_name")).thenReturn(null);
	        Mockito.when(superHeroRepository.findById(sh1.getId())).thenReturn(Optional.of(sh1));
	        Mockito.when(superHeroRepository.findAll()).thenReturn(allSuperHeros);
	        Mockito.when(superHeroRepository.findById(-99L)).thenReturn(Optional.empty());
	    }
	    

	    private void verifyFindBySuperHeroNameIsCalledOnce(String superHeroName) {
	        Mockito.verify(superHeroRepository, VerificationModeFactory.times(1)).findBySuperHeroName(superHeroName);
	        Mockito.reset(superHeroRepository);
	    }

	    private void verifyFindByIdIsCalledOnce() {
	        Mockito.verify(superHeroRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
	        Mockito.reset(superHeroRepository);
	    }

	    private void verifyFindAllEmployeesIsCalledOnce() {
	        Mockito.verify(superHeroRepository, VerificationModeFactory.times(1)).findAll();
	        Mockito.reset(superHeroRepository);
	    }
	    
	    @Test
	    public void whenValidId_thenSuperHeroMustBeFound() {
	        Optional<SuperHero> found = superHeroService.retrieveSuperHeroById(-1L);
	        verifyFindByIdIsCalledOnce();
	        assertThat(found.isPresent() && found.get().getSuperHeroName().equals("superHeroName1"));
	    }

	    @Test
	    public void whenInValidId_thenSuperHeroMustNotBeFound() {
	        Optional<SuperHero> found = superHeroService.retrieveSuperHeroById(-100L);
	        verifyFindByIdIsCalledOnce();
	        assertThat(!found.isPresent());
	    }
	    	    
	    @Test
	    public void whenValidSuperHeroName_thenSuperHeroMustBeFound() {
	        String superHeroName = "superHeroName1";
	        Optional<SuperHero> found = superHeroService.retrieveSuperHerosBySuperHeroName(superHeroName);
	        assertThat(found.isPresent() && found.get().getSuperHeroName().equals(superHeroName));
	    }

	    @Test
	    public void whenInValidSuperHeroName_thenSuperHeroMustNotBeFound() {
	        Optional<SuperHero> found = superHeroService.retrieveSuperHerosBySuperHeroName("wrong_name");
	        assertThat(found.empty());
	        verifyFindBySuperHeroNameIsCalledOnce("wrong_name");
	    }

	    @Test
	    public void givenAllSuperHeros_whengetAll_thenReturn3Records() {
	        SuperHero sh1 = new SuperHero("firstName1", "lastName1", "superHeroName1");
	        SuperHero sh2 = new SuperHero("firstName2", "lastName2", "superHeroName2");
	        SuperHero sh3 = new SuperHero("firstName3", "lastName3", "superHeroName3");
	        List<SuperHero> all = superHeroService.retrieveAllSuperHeros();
	        verifyFindAllEmployeesIsCalledOnce();
	        assertThat(all).hasSize(3).extracting(SuperHero::getSuperHeroName).contains(sh1.getSuperHeroName(), sh2.getSuperHeroName(), sh3.getSuperHeroName());
	    }
	}
