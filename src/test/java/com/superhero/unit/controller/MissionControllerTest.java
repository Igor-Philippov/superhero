package com.superhero.unit.controller;

import static com.superhero.rest.constant.Paths.MISSIONS;
import static com.superhero.rest.constant.Paths.MISSION_BY_ID;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.superhero.unit.controller.BaseControllerTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superhero.model.Mission;
import com.superhero.rest.controller.MissionController;

@WebMvcTest(MissionController.class)
public class MissionControllerTest extends BaseControllerTest {
    Logger logger = LoggerFactory.getLogger(MissionControllerTest.class); 
	
	@Autowired
    protected MockMvc mvc;

    @MockBean
    private MissionController missionController;
    
    Mission mission = new Mission("mission1000"); {
    	mission.setCompleted(true);
    	mission.setId(1000L);
    }
    
    @Test
    public void retrieveMissionByIdTest() throws Exception {
		given(missionController.retrieveMissionById(mission.getId())).willReturn(ResponseEntity.ok().body(mission));   	
		mvc.perform(get(MISSION_BY_ID, mission.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.name", is(mission.getName())))
				.andExpect(jsonPath("$.completed", is(mission.isCompleted())));
    	
		verify(missionController, times(1)).retrieveMissionById(mission.getId());
		verifyNoMoreInteractions(missionController);    	
    }
    
	@Test
	public void retrieveAllMissionsTest() throws Exception {
		List<Mission> missions = singletonList(mission);
		given(missionController.retrieveAllMissions()).willReturn(missions);
		mvc.perform(get(MISSIONS).
				contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is(mission.getName())))
				.andExpect(jsonPath("$[0].completed", is(mission.isCompleted())));
		
		verify(missionController, times(1)).retrieveAllMissions();
		verifyNoMoreInteractions(missionController);  		
	}
    
	@Test
	public void createMissionTest() throws Exception {
		//given(missionController.createMission(mission)).willReturn(ResponseEntity.ok().body(mission));
		mvc.perform(post(MISSIONS)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(toJsonString(mission)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", is(mission.getName())))
				.andExpect(jsonPath("$[0].completed", is(mission.isCompleted())));
	}
	
//	@Test
//	public void retrieveAllMissionsTest() throws Exception {
//
//		List<Mission> missions = new ArrayList<Mission>();
//		missions.add(mission);
//
//		when(missionResource.retrieveAllMissions()).thenReturn(missions);
//
//		mvc.perform(get("/missions/list").
//				contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(1)))
//				.andExpect(jsonPath("$[0].name", is("mission")))
//				.andExpect(jsonPath("$[0].isCompleted", is(false)))
//				.andExpect(jsonPath("$[0].isDeleted", is(false)));
//		
//		verify(missionResource, times(1)).retrieveAllMissions();
//		verifyNoMoreInteractions(missionResource);  		
//
//	}
    
    
//	@Test
//	public void updateMissionTest() throws Exception {
//
//		doNothing().when(missionResource).updateMission(mission, mission.getId());
//
//		mvc.perform(put("/missions/update/{id}", mission.getId())
//				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//				.content(asJsonString(mission)))
//				.andExpect(status().isOk());
//
//	} 
    
	@Test
	public void deleteMissionTest() throws Exception {	
		given(missionController.softDeleteMission(mission.getId())).willReturn(ResponseEntity.noContent().build());
        mvc.perform(delete(MISSION_BY_ID, mission.getId())
        		.with(user("shc_user").password("password"))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());
		
		verify(missionController, times(1)).softDeleteMission(mission.getId());
		verifyNoMoreInteractions(missionController);
	}
}
