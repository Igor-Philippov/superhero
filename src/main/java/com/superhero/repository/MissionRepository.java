package com.superhero.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superhero.model.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
	
    /** Soft deletes a mission by its Id. */
	@Query("update #{#entityName} e set e.isDeleted = 1 where e.id = ?1")
	@Modifying
	@Transactional
	public void softDeleteById(Long id);
	
    /** Overridden version of [findById(Long id)] to consider soft deleted missions. */
	@Override
	@Query("select e from #{#entityName} e where e.id = ?1 and e.isDeleted = 0")
	public Optional<Mission> findById(Long id);
	

    /**
     * Overridden version of [findAll()] to consider soft deleted missions.
     * @return  A list of all missions. If no mission is found, this method returns an empty list.
     */
	@Override
	@Query("select e from #{#entityName} e where e.isDeleted = 0")
	public List<Mission> findAll();
   
    /**
     * Finds missions by its case-insensitive name as a search criteria excluding soft deleted ones.
     * @param name  
     * @return  A list of missions which name is a case-insensitive match with the given name.
     *          If no mission is found, this method returns an empty list.
     */
	@Query("select e from #{#entityName} e where LOWER(e.name) = LOWER(:name) and e.isDeleted = 0")
    public Optional<Mission> findByName(@Param("name") String name);
    
    /**
     * Finds all completed missions excluding soft deleted ones.
     * @return  A list of completed missions. If no mission is found, this method returns an empty list.
     */
    @Query("select e from #{#entityName} e where e.isDeleted = 0 and e.isCompleted = 1") 
    public List<Mission> findAllCompleted();
    
    /**
     * Finds all deleted missions.
     * @return  A list of completed missions. If no mission is found, this method returns an empty list.
     */   
    @Query("select e from #{#entityName} e where e.isDeleted = 1")
    @Modifying
    public List<Mission> findAllDeleted();
}
