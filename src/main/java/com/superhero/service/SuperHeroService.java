package com.superhero.service;

import java.util.List;
import java.util.Optional;

import com.superhero.model.Mission;
import com.superhero.model.SuperHero;

public interface SuperHeroService {
	
	public SuperHero saveSuperHero(SuperHero superHero);
	
	public Optional<SuperHero> retrieveSuperHeroById(Long id);
	
	public Optional<SuperHero> retrieveSuperHerosBySuperHeroName(String superHeroName);
	
	public List<SuperHero> retrieveSuperHerosByFirstName(String firstName);
	
	public List<SuperHero> retrieveSuperHerosByLastName(String lastName);
	
	public List<SuperHero> retrieveAllSuperHeros();
	
	public void deleteSuperHeroById(Long id);
	
	
	public Mission saveMission(Mission mission);
	
	public void softDeleteMission(Long id);
	
	public Optional<Mission> retrieveMissionById(Long id);
	
	public Optional<Mission> retrieveMissionByName(String name);
		
	public List<Mission> retrieveAllMissions();
	
	public List<Mission> retrieveAllMissionsCompleted();
	
	public List<Mission> retrieveAllMissionsDeleted();
}
