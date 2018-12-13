package com.superhero.rest.endpoint;

import static com.superhero.rest.constant.Paths.SUPERHEROS;
import static com.superhero.rest.constant.Paths.SUPERHEROS_BY_FIRSTNAME;
import static com.superhero.rest.constant.Paths.SUPERHEROS_BY_LASTNAME;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_ID;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_SUPERHERONAME;
import static com.superhero.rest.constant.Paths.VERSION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
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

import com.superhero.model.Mission;
import com.superhero.model.SuperHero;
import com.superhero.rest.exception.SuperHeroNotFoundException;
import com.superhero.service.SuperHeroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = VERSION + SUPERHEROS)
public class SuperHeroEndpoint {
	
	@Autowired
	private SuperHeroService superHeroService;
	
	@ApiOperation("Creates a superhero")
	@PostMapping(value = SUPERHEROS, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceSupport> createSuperHero(@RequestBody SuperHero superHero) {
		SuperHero savedSuperHero = superHeroService.saveSuperHero(superHero);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedSuperHero.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@ApiOperation("Retrieves a superhero by ID")
	@GetMapping(value = SUPERHERO_BY_ID, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuperHero> retrieveSuperHeroById(@PathVariable Long id) {
		return superHeroService.retrieveSuperHeroById(id)
				// 200 OK
				.map(superHero -> ResponseEntity.ok().body(superHero))
				// 404 Not found
				.orElseThrow(() -> new SuperHeroNotFoundException("No registered Super Hero with ID = " + id));
	}

	@ApiOperation("Retrieves a superhero by unique superhero name")
	@GetMapping(value = SUPERHERO_BY_SUPERHERONAME, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuperHero> retrieveSuperHeroBySuperHeroName(@PathVariable String superHeroName) {
		return superHeroService.retrieveSuperHerosBySuperHeroName(superHeroName)
				// 200 OK
				.map(superHero -> ResponseEntity.ok().body(superHero))
				// 404 Not found
				.orElseThrow(() -> new SuperHeroNotFoundException("No registered Super Hero by a superHeroName = " + superHeroName));
	}
	
	@ApiOperation("Retrieves a list of superheros by first name")
	@GetMapping(value = SUPERHEROS_BY_FIRSTNAME, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<SuperHero>> retrieveSuperHerosByFirstName(@PathVariable String firstName) {
		return ResponseEntity.ok().body(superHeroService.retrieveSuperHerosByFirstName(firstName));
	}
	
	@ApiOperation("Retrieves a list of superheros by last name")
	@GetMapping(value = SUPERHEROS_BY_LASTNAME, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<SuperHero>> retrieveSuperHerosByLastName(@PathVariable String lastName) {
		return ResponseEntity.ok().body(superHeroService.retrieveSuperHerosByLastName(lastName));
	}
	
	@ApiOperation("Retrieves all superheros")
	@GetMapping(value = SUPERHEROS, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<SuperHero>> retrieveAllSuperHeros() {
		return ResponseEntity.ok().body(superHeroService.retrieveAllSuperHeros());
	}
	
	@ApiOperation("Updates a superhero")
    @PutMapping(value = SUPERHERO_BY_ID, produces = APPLICATION_JSON_VALUE)
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
			return ResponseEntity.ok().body(superHeroService.saveSuperHero(superHero));
		}
		else {
			throw new SuperHeroNotFoundException("No registered Super Hero with ID = " + id + " available for update");
		}
	}
	
	@ApiOperation(value = "Deletes a superhero by ID")
	@DeleteMapping(value = SUPERHERO_BY_ID, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceSupport> deleteSuperHero(@PathVariable Long id) {
		superHeroService.deleteSuperHeroById(id);
		return ResponseEntity.noContent().build();
	}
}
