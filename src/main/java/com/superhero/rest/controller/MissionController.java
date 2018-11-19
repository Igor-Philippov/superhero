package com.superhero.rest.controller;

import static com.superhero.rest.constant.Paths.MISSIONS;
import static com.superhero.rest.constant.Paths.MISSIONS_COMPLETED;
import static com.superhero.rest.constant.Paths.MISSIONS_DELETED;
import static com.superhero.rest.constant.Paths.MISSION_BY_ID;
import static com.superhero.rest.constant.Paths.MISSION_BY_NAME;

import java.net.URI;
import java.util.List;

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
import com.superhero.model.Mission;
import com.superhero.model.SuperHero;
import com.superhero.service.SuperHeroService;

import io.swagger.annotations.ApiOperation;

@RestController
public class MissionController {
	
	@Autowired
	private SuperHeroService superHeroService;
	
	@ApiOperation(value = "Create a mission")
	@PostMapping(MISSIONS)
	public ResponseEntity<SuperHero> createMission(@RequestBody Mission mission) {
		Mission savedMission = superHeroService.saveMission(mission);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedMission.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
//	@GetMapping("/employees/{id}")
//	Resource<Employee> one(@PathVariable Long id) {
//
//		Employee employee = repository.findById(id)
//			.orElseThrow(() -> new EmployeeNotFoundException(id));
//
//		return new Resource<>(employee,
//			linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
//			linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
//	}

	@ApiOperation(value = "Retrieve a mission by ID")
	@GetMapping(MISSION_BY_ID)
	public ResponseEntity<Mission> retrieveMissionById(@PathVariable Long id) {
	    return superHeroService.retrieveMissionById(id)
	            .map( mission -> ResponseEntity.ok().body(mission) )     //200 OK
	            .orElseThrow( () -> new MissionNotFoundException("No registered Mission with ID = " + id)); //404 Not found
	}
	
	@ApiOperation(value = "Retrieve a mission by name")
	@GetMapping(MISSION_BY_NAME)
	public ResponseEntity<Mission> retrieveMissionByName(@PathVariable String name) {
	    return superHeroService.retrieveMissionByName(name)
	            .map( mission -> ResponseEntity.ok().body(mission) )     //200 OK
	            .orElseThrow( () -> new MissionNotFoundException("No registered Mission with name = " + name)); //404 Not found
	}
    
	@ApiOperation(value = "Retrieve all completeed missions")
	@GetMapping(MISSIONS_COMPLETED)
	public List<Mission> retrieveAllMissionsCompleted() {
		return superHeroService.retrieveAllMissionsCompleted();
	}
	
	@ApiOperation(value = "Retrieve all deleted missions")
	@GetMapping(MISSIONS_DELETED)
	public List<Mission> retrieveAllMissionsDeleted() {
		return superHeroService.retrieveAllMissionsDeleted();
	}
	
//	@GetMapping("/employees")
//	Resources<Resource<Employee>> all() {
//
//		List<Resource<Employee>> employees = repository.findAll().stream()
//			.map(employee -> new Resource<>(employee,
//				linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
//				linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
//			.collect(Collectors.toList());
//
//		return new Resources<>(employees,
//			linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
//	}
	
	@ApiOperation(value = "Retrieve all missions")
	@GetMapping(MISSIONS)
	public List<Mission> retrieveAllMissions() {
		return superHeroService.retrieveAllMissions();
	}
	
//	@PutMapping("/missions/{id}")
//	public ResponseEntity<Mission> updateMission(@RequestBody Mission mission, @PathVariable Long id) {
//		if (superHeroService.retrieveMissionById(id).isPresent()) {
//			mission.setId(id);
//			superHeroService.saveMission(mission);
//			return ResponseEntity.ok().build();
//		}
//		else {
//			throw new MissionNotFoundException("No registered Mission with ID = " + id + " available for update");
//		}
//	}
	
	@PutMapping(MISSION_BY_ID)
	@ApiOperation(value = "Update a mission")
	Mission updateMission(@RequestBody Mission newMission, @PathVariable Long id) {

		return superHeroService.retrieveMissionById(id)
			.map(mission -> {
				mission.setName(newMission.getName());
				mission.setCompleted(newMission.isCompleted());
				return superHeroService.saveMission(mission);
			})
			.orElseGet(() -> {
				newMission.setId(id);
				return superHeroService.saveMission(newMission);
			});
	}
	
//	@PutMapping("/employees/{id}")
//	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//
//		return repository.findById(id)
//			.map(employee -> {
//				employee.setName(newEmployee.getName());
//				employee.setRole(newEmployee.getRole());
//				return repository.save(employee);
//			})
//			.orElseGet(() -> {
//				newEmployee.setId(id);
//				return repository.save(newEmployee);
//			});
//	}

	@ApiOperation(value = "Delete a mission by ID")
	@DeleteMapping(MISSION_BY_ID)
	ResponseEntity<ResourceSupport> softDeleteMission(@PathVariable Long id) {	
		if (superHeroService.retrieveMissionById(id).isPresent()) {
			superHeroService.softDeleteMission(id);
			return ResponseEntity.noContent().build();
		}
		else {
			throw new MissionNotFoundException("No registered Mission with ID = " + id);
		}
	}
}
