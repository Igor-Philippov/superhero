package com.superhero.rest.endpoint;

import static com.superhero.rest.constant.Paths.MISSIONS;
import static com.superhero.rest.constant.Paths.MISSIONS_BY_NAME;
import static com.superhero.rest.constant.Paths.MISSIONS_COMPLETED;
import static com.superhero.rest.constant.Paths.MISSIONS_DELETED;
import static com.superhero.rest.constant.Paths.MISSION_BY_ID;
import static com.superhero.rest.constant.Paths.VERSION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superhero.model.Mission;
import com.superhero.rest.exception.MissionNotFoundException;
import com.superhero.service.SuperHeroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = VERSION + MISSIONS)
public class MissionEndpoint {
	
	@Autowired
	private SuperHeroService superHeroService;
	
	@ApiOperation("Creates a mission")
	@PostMapping(value = MISSIONS, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceSupport> createMission(@RequestBody Mission mission) {
		Mission savedMission = superHeroService.saveMission(mission);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedMission.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@ApiOperation("Retrieves a mission by ID")
	@GetMapping(value = MISSION_BY_ID, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Mission> retrieveMissionById(@PathVariable Long id) {
		return superHeroService.retrieveMissionById(id)
				// 200 OK
				.map(mission -> ResponseEntity.ok().body(mission))
				// 404 Not found
				.orElseThrow(() -> new MissionNotFoundException("No registered Mission with ID = " + id));
	}

	@ApiOperation("Retrieves a list of missions by name")
	@GetMapping(value = MISSIONS_BY_NAME, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Mission>> retrieveMissionByName(@PathVariable String name) {
		return ResponseEntity.ok().body(superHeroService.retrieveMissionByName(name));
	}
    
	@ApiOperation("Retrieves all completeed missions")
	@GetMapping(value = MISSIONS_COMPLETED, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Mission>> retrieveAllMissionsCompleted() {
		return ResponseEntity.ok().body(superHeroService.retrieveAllMissionsCompleted());
	}
	
	@ApiOperation("Retrieves all deleted missions")
	@GetMapping(value = MISSIONS_DELETED, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Mission>> retrieveAllMissionsDeleted() {
		return ResponseEntity.ok().body(superHeroService.retrieveAllMissionsDeleted());
	}
	
	@ApiOperation("Retrieves all missions")
	@GetMapping(value = MISSIONS, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Mission>> retrieveAllMissions() {
		return ResponseEntity.ok().body(superHeroService.retrieveAllMissions());
	}
	
	@ApiOperation("Updates a mission")
	@PutMapping(value = MISSION_BY_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Mission> updateMission(@RequestBody Mission newMission, @PathVariable Long id) {
		return superHeroService.retrieveMissionById(id)
			.map(mission -> {
				mission.setName(newMission.getName());
				mission.setCompleted(newMission.isCompleted());
				return ResponseEntity.ok().body(superHeroService.saveMission(mission));
			})
			.orElseGet(() -> {
				newMission.setId(id);
				return ResponseEntity.ok().body(superHeroService.saveMission(newMission));
			});
	}

	@ApiOperation(value = "Deletes a mission by ID")
	@DeleteMapping(value = MISSION_BY_ID, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceSupport> softDeleteMission(@PathVariable Long id) {	
		if (superHeroService.retrieveMissionById(id).isPresent()) {
			superHeroService.softDeleteMission(id);
			return ResponseEntity.noContent().build();
		}
		else {
			throw new MissionNotFoundException("No registered for deletion Mission with ID = " + id);
		}
	}
}
