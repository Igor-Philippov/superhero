package com.superhero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.superhero.model.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

}
