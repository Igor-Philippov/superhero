package com.superhero.service;

import java.util.List;

import com.superhero.model.Mission;
import com.superhero.model.SuperHero;

public interface SuperHeroService {
	
	public SuperHero saveSuperHero(SuperHero superHero);
	
	public SuperHero retrieveSuperHeroById(Long id);
	
	public SuperHero retrieveSuperHerosBySuperHeroName(String superHeroName);
	
	public List<SuperHero> retrieveSuperHerosByFirstName(String firstName);
	
	public List<SuperHero> retrieveSuperHerosByLastName(String lastName);
	
	public List<SuperHero> retrieveAllSuperHeros();
	
	public void deleteSuperHeroById(Long id);
	
	
	public Mission saveMission(Mission mission);
	
	public Mission retrieveMissionById(Long id);
	
	public Mission retrieveMissionByName(String name);
		
	public List<Mission> retrieveAllMissions();
	
	public List<Mission> retrieveAllMissionsCompleted();
	
	public List<Mission> retrieveAllMissionsDeleted();
}
