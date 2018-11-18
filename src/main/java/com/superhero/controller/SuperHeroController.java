package com.superhero.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.superhero.exception.MissionNotFoundException;
import com.superhero.exception.MissionUnprocessableException;
import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.model.Mission;
import com.superhero.model.SuperHero;
import com.superhero.service.SuperHeroService;

@RestController
public class SuperHeroController {
	
	@Autowired
	private SuperHeroService superHeroService;
	
	@PostMapping("/superheros")
	public ResponseEntity<SuperHero> createSuperHero(@RequestBody SuperHero superHero) {
		SuperHero savedSuperHero = superHeroService.saveSuperHero(superHero);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedSuperHero.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/superheros/{id}")
	public ResponseEntity<SuperHero> retrieveSuperHeroById(@PathVariable Long id) {
	    return Optional.ofNullable( superHeroService.retrieveSuperHeroById(id) )
	            .map( superHero -> ResponseEntity.ok().body(superHero) )     //200 OK
	            .orElseThrow( () -> new SuperHeroNotFoundException("No registered Super Hero with ID = " + id)); //404 Not found
	}
	
	@GetMapping("/superheros/names/superhero/{superHeroName}")
	public ResponseEntity<SuperHero> retrieveSuperHeroBySuperHeroName(@PathVariable String superHeroName) {
		SuperHero superHero = superHeroService.retrieveSuperHerosBySuperHeroName(superHeroName);
		if (superHero != null) {
			return ResponseEntity.ok().body(superHero);   //200 OK
		}
		else {
			throw new SuperHeroNotFoundException("No registered Super Hero by a superHeroName = " + superHeroName); //404 Not found
		}
	}
	
	@GetMapping("/superheros/names/first/{firstName}")
	public List<SuperHero> retrieveSuperHerosByFirstName(@PathVariable String firstName) {
		return superHeroService.retrieveSuperHerosByFirstName(firstName);
	}
	
	@GetMapping("/superheros/names/last/{lastName}")
	public List<SuperHero> retrieveSuperHerosByLastName(@PathVariable String lastName) {
		return superHeroService.retrieveSuperHerosByLastName(lastName);
	}
	
	@GetMapping("/superheros")
	public List<SuperHero> retrieveAllSuperHeros() {
		return superHeroService.retrieveAllSuperHeros();
	}
	

    @PutMapping("/superheros/{id}")
	public ResponseEntity<SuperHero> updateSuperHero(@RequestBody SuperHero superHero, @PathVariable Long id) {
		SuperHero superHeroSaved = superHeroService.retrieveSuperHeroById(id);
		if (superHeroSaved != null) {
			superHero.setId(id);
			// Requirements: [Unable to remove a completed mission]
			for (Mission missionToKeep : superHeroSaved.getMissions().stream().filter(mission -> mission.isCompleted()).collect(Collectors.toList())) {
				boolean isFound = false;
				for (Mission missionForUpdate : superHero.getMissions()) {
					if (missionForUpdate.getId().equals(missionToKeep.getId()) || missionForUpdate.getName().equalsIgnoreCase(missionToKeep.getName())) {
						// A match is found in the missions to be saved -> keeping main info from it but preserving original Id and isCompleted
						isFound = true;
						missionForUpdate.setId(missionToKeep.getId());
						missionForUpdate.setCompleted(true);
					}
				}
				if (!isFound) { 
					// No match found -> keeping the one from the database as is
					superHero.getMissions().add(missionToKeep);
				}
			}
			
			superHeroService.saveSuperHero(superHero);
			return ResponseEntity.ok().build();
		}
		else {
			throw new SuperHeroNotFoundException("No registered Super Hero with ID = " + id + " available for update");
		}
	}
	
	@DeleteMapping("/superheros/{id}")
	ResponseEntity<Void> deleteSuperHero(@PathVariable Long id) {
		superHeroService.deleteSuperHeroById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/missions")
	public ResponseEntity<SuperHero> createMission(@RequestBody Mission mission) {
		Mission savedMission = superHeroService.saveMission(mission);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedMission.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/missions/{id}")
	public ResponseEntity<Mission> retrieveMissionById(@PathVariable Long id) {
	    return Optional.ofNullable( superHeroService.retrieveMissionById(id) )
	            .map( mission -> ResponseEntity.ok().body(mission) )     //200 OK
	            .orElseThrow( () -> new MissionNotFoundException("No registered Mission with ID = " + id)); //404 Not found
	}
		
	@GetMapping("/missions/completed")
	public List<Mission> retrieveAllMissionsCompleted() {
		return superHeroService.retrieveAllMissionsCompleted();
	}
	
	@GetMapping("/missions/deleted")
	public List<Mission> retrieveAllMissionsDeleted() {
		return superHeroService.retrieveAllMissionsDeleted();
	}
	
	@GetMapping("/missions")
	public List<Mission> retrieveAllMissions() {
		return superHeroService.retrieveAllMissions();
	}
	
	@PutMapping("/missions/{id}")
	public ResponseEntity<Mission> updateMission(@RequestBody Mission mission, @PathVariable Long id) {
		Mission presentMission = superHeroService.retrieveMissionById(id);
		if (presentMission != null) {
			mission.setId(id);
			superHeroService.saveMission(mission);
			return ResponseEntity.ok().build();
		}
		else {
			throw new MissionNotFoundException("No registered Mission with ID = " + id + " available for update");
		}
	}

	@DeleteMapping("/missions/softdelete/{id}")
	ResponseEntity<ResourceSupport> softDeleteMission(@PathVariable Long id) {	
		Mission mission = superHeroService.retrieveMissionById(id);
		if (mission != null) {
			if (mission.isDeleted() == false) {
				mission.setDeleted(true);
				superHeroService.saveMission(mission);
				return ResponseEntity.noContent().build();
			}
			else {
				throw new MissionUnprocessableException("Mission with ID = " + id + " is already soft deleted");
			}
		}
		else {
			throw new MissionNotFoundException("No registered Mission with ID = " + id);
		}
	}
}
