package com.superhero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superhero.model.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
   
    /**
     * Finds missions by its case-insensitive name as a search criteria.
     * @param name  
     * @return  A list of missions which name is a case-insensitive match with the given name.
     *          If no mission is found, this method returns an empty list.
     */
    @Query("SELECT m FROM Mission m where LOWER(m.name) = LOWER(:name)") 
    public List<Mission> findByName(@Param("name") String name);
    
    
    /**
     * Finds all completed missions.
     * @return  A list of completed missions. If no mission is found, this method returns an empty list.
     */
    @Query("SELECT m FROM Mission m where m.isCompleted = 1") 
    public List<Mission> findAllCompleted();
    
    /**
     * Finds all deleted missions.
     * @return  A list of completed missions. If no mission is found, this method returns an empty list.
     */
    @Query("SELECT m FROM Mission m where m.isDeleted = 1") 
    public List<Mission> findAllDeleted();

}
