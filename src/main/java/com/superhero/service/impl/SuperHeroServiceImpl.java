package com.superhero.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superhero.model.Mission;
import com.superhero.model.SuperHero;
import com.superhero.repository.MissionRepository;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.service.SuperHeroService;

@Service
public class SuperHeroServiceImpl implements SuperHeroService {

    private static final Logger logger = LoggerFactory.getLogger(SuperHeroServiceImpl.class);
	
	@Autowired
	private SuperHeroRepository superHeroRepository;
	
	@Autowired
	private MissionRepository missionRepository;
	
	@Override
	public SuperHero saveSuperHero(SuperHero superHero) {
		logger.debug("CALLING saveSuperHero(" + superHero + ")");
		return superHeroRepository.save(superHero);
	}
	
	@Override
	public SuperHero retrieveSuperHeroById(Long id) {
		logger.debug("CALLING retrieveSuperHeroById(" + id + ")");
		Optional<SuperHero> superHero = superHeroRepository.findById(id);
		return superHero.isPresent() ? superHero.get() : null;
	}
	
	@Override
	public SuperHero retrieveSuperHerosBySuperHeroName(String superHeroName) {
		Optional<SuperHero> superHero = superHeroRepository.findBySuperHeroName(superHeroName);
		return superHero.isPresent() ? superHero.get() : null;
	}
	
	@Override
	public List<SuperHero> retrieveSuperHerosByFirstName(String firstName) {
		logger.debug("CALLING retrieveSuperHerosByFirstName(" + firstName + ")");
		return superHeroRepository.findByFirstName(firstName);
	}
	
	@Override
	public List<SuperHero> retrieveSuperHerosByLastName(String lastName) {
		logger.debug("CALLING retrieveSuperHerosByLastName(" + lastName + ")");
		return superHeroRepository.findByLastName(lastName);
	}
	
	@Override
	public List<SuperHero> retrieveAllSuperHeros() {
		return superHeroRepository.findAll();
	}
	
	@Override
	public void deleteSuperHeroById(Long id) {
		logger.debug("CALLING deleteSuperHeroById(" + id + ")");
		superHeroRepository.deleteById(id);
	}
		
	@Override
	public Mission saveMission(Mission mission) {
		logger.debug("CALLING saveMission(" + mission + ")");
		return missionRepository.save(mission);
	}
	
	@Override
	public Mission retrieveMissionById(Long id) {
		logger.debug("CALLING retrieveSuperHeroById(" + id + ")");
		Optional<Mission> mission = missionRepository.findById(id);
		return mission.isPresent() ? mission.get() : null;
	}
	
	@Override
	public Mission retrieveMissionByName(String name) {
		logger.debug("CALLING retrieveMissionByName(" + name + ")");
		Optional<Mission> mission = missionRepository.findByName(name);
		return mission.isPresent() ? mission.get() : null;
	}
		
	@Override
	public List<Mission> retrieveAllMissions() {
		return missionRepository.findAll();
	}
	
	@Override
	public List<Mission> retrieveAllMissionsCompleted() {
		return missionRepository.findAllCompleted();
	}
	
	@Override
	public List<Mission> retrieveAllMissionsDeleted() {
		return missionRepository.findAllDeleted();
	}
}
