package com.superhero.test.unit.rest;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superhero.model.Mission;
import com.superhero.model.SuperHero;
import com.superhero.rest.endpoint.SuperHeroEndpoint;
import com.superhero.security.SecurityConfig;
import com.superhero.test.BaseUnitTest;

@WebMvcTest(SuperHeroEndpoint.class)
@ContextConfiguration(classes={com.superhero.config.ApplicationConfig.class, SecurityConfig.class})
public abstract class AbstractEndpointUnitTest extends BaseUnitTest {
	
	protected MockMvc mvc;
    
    @Autowired
    protected ObjectMapper objectMapper;
    
    protected static final Long idNotFound = 0L;
    protected static SuperHero superHero1, superHero1Updated, superHero2;
    protected static Mission mission1, mission1Updated, missionNotFound, mission2, mission3, mission10, mission11, mission13;
    protected static Iterable<SuperHero> superHeros;
    protected static Iterable<Mission> missionsCompleted;
    
    @BeforeClass
    public static void setUpClass() {    	
        mission1 = new Mission("missionName1");
        mission1.setId(-1L);
        
        mission1Updated = new Mission("missionName1Updated");
        mission1Updated.setCompleted(true);
        mission1Updated.setId(-1L);
        
        missionNotFound = new Mission("missionNameNotFound");
        missionNotFound.setCompleted(true);
        missionNotFound.setId(idNotFound);
        
        mission2 = new Mission("missionName2");
        mission2.setId(-2L);
        
        mission3 = new Mission("missionName3");
        mission3.setId(-3L);
        
        mission10 = new Mission("missionName");
        mission10.setCompleted(true);
        mission10.setId(-10L);
        
        mission11 = new Mission("missionName");
        mission11.setCompleted(true);
        mission11.setId(-11L);
        
        mission13 = new Mission("missionName13");
        mission13.setDeleted(true);
        mission13.setCompleted(true);
        mission13.setId(-13L);
    	
    	superHero1 = new SuperHero("firstName_NEW", "lastName_NEW", "superHeroName_NEW1");
    	superHero1.setId(-100L);
    	superHero1.addMission(mission1);
    	superHero1.addMission(mission2);
    	
    	superHero1Updated = new SuperHero("firstName_UPDATED1", "lastName_UPDATED1", "superHeroName_UPDATED1");
    	superHero1Updated.setId(-100L);
    	superHero1Updated.addMission(mission1);
    	
    	superHero2 = new SuperHero("FIRSTNAME_new", "LASTNAME_new", "superHeroName_NEW2");
    	superHero2.setId(-200L);
    	superHero2.addMission(mission3);
    	
    	superHeros = Arrays.asList(superHero1, superHero2);
    	
    	missionsCompleted = Arrays.asList(mission10, mission11);
    }
	
//	@EnableWebSecurity
//	@EnableWebMvc
//	static class Config extends WebSecurityConfigurerAdapter {
//		
//		@Autowired
//		private ApplicationConfig config;
//
//		// @formatter:off
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http
//				.authorizeRequests()
//					.antMatchers("/admin/**").hasRole("ADMIN")
//					.anyRequest().authenticated()
//					.and()
//				.formLogin();
//		}
//		// @formatter:on
//
//		// @formatter:off
//		@Autowired
//		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//			auth
//				.inMemoryAuthentication()
//					.withUser("user").password("password").roles("USER");
//		}
//		// @formatter:on
//
//		@Override
//		@Bean
//		public UserDetailsService userDetailsServiceBean() throws Exception {
//			return super.userDetailsServiceBean();
//		}		
//	}
}
