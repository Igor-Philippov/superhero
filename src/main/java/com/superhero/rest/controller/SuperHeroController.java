package com.superhero.rest.controller;

import static com.superhero.rest.constant.Paths.SUPERHEROS;
import static com.superhero.rest.constant.Paths.SUPERHEROS_BY_FIRSTNAME;
import static com.superhero.rest.constant.Paths.SUPERHEROS_BY_LASTNAME;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_ID;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_SUPERHERONAME;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.model.Mission;
import com.superhero.model.SuperHero;
import com.superhero.service.SuperHeroService;

@RestController
public class SuperHeroController {
	
	@Autowired
	private SuperHeroService superHeroService;
	
	@PostMapping(SUPERHEROS)
	public ResponseEntity<SuperHero> createSuperHero(@RequestBody SuperHero superHero) {
		SuperHero savedSuperHero = superHeroService.saveSuperHero(superHero);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedSuperHero.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping(SUPERHERO_BY_ID)
	public ResponseEntity<SuperHero> retrieveSuperHeroById(@PathVariable Long id) {
	    return superHeroService.retrieveSuperHeroById(id)
	            .map( superHero -> ResponseEntity.ok().body(superHero) )     //200 OK
	            .orElseThrow( () -> new SuperHeroNotFoundException("No registered Super Hero with ID = " + id)); //404 Not found
	}
	
	@GetMapping(SUPERHERO_BY_SUPERHERONAME)
	public ResponseEntity<SuperHero> retrieveSuperHeroBySuperHeroName(@PathVariable String superHeroName) {
	    return superHeroService.retrieveSuperHerosBySuperHeroName(superHeroName)
	            .map( superHero -> ResponseEntity.ok().body(superHero) )     //200 OK
	            .orElseThrow( () -> new SuperHeroNotFoundException("No registered Super Hero by a superHeroName = " + superHeroName)); //404 Not found
	}
	
	@GetMapping(SUPERHEROS_BY_FIRSTNAME)
	public List<SuperHero> retrieveSuperHerosByFirstName(@PathVariable String firstName) {
		return superHeroService.retrieveSuperHerosByFirstName(firstName);
	}
	
	@GetMapping(SUPERHEROS_BY_LASTNAME)
	public List<SuperHero> retrieveSuperHerosByLastName(@PathVariable String lastName) {
		return superHeroService.retrieveSuperHerosByLastName(lastName);
	}
	
	@GetMapping(SUPERHEROS)
	public List<SuperHero> retrieveAllSuperHeros() {
		return superHeroService.retrieveAllSuperHeros();
	}
	

    @PutMapping(SUPERHERO_BY_ID)
	public ResponseEntity<SuperHero> updateSuperHero(@RequestBody SuperHero superHero, @PathVariable Long id) {
		Optional<SuperHero> superHeroSaved = superHeroService.retrieveSuperHeroById(id);
		if (superHeroSaved.isPresent()) {
			superHero.setId(id);
			// Requirements: [Unable to remove a completed mission]
			for (Mission missionToKeep : superHeroSaved.get().getMissions().stream().filter(mission -> mission.isCompleted()).collect(Collectors.toList())) {
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
	
	@DeleteMapping(SUPERHERO_BY_ID)
	ResponseEntity<Void> deleteSuperHero(@PathVariable Long id) {
		superHeroService.deleteSuperHeroById(id);
		return ResponseEntity.noContent().build();
	}
}
