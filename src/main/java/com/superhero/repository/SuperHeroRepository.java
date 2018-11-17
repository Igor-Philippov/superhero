package com.superhero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superhero.model.SuperHero;

@Repository
public interface SuperHeroRepository extends JpaRepository<SuperHero, Long> {
	
    /**
     * Finds super heros by his/her case-insensitive first name as a search criteria.
     * @param name  
     * @return  A list of super heros which first name is a case-insensitive match with the given name.
     *          If no persons is found, this method returns an empty list.
     */
    @Query("SELECT s FROM SuperHero s where LOWER(s.firstName) = LOWER(:firstName)") 
    public List<SuperHero> findByFirstName(@Param("firstName") String firstName);
    
    /**
     * Finds super heros by his/her case-insensitive last name as a search criteria.
     * @param name  
     * @return  A list of super heros which last name is a case-insensitive match with the given name.
     *          If no persons is found, this method returns an empty list.
     */
    @Query("SELECT s FROM SuperHero s where LOWER(s.lastName) = LOWER(:lastName)") 
    public List<SuperHero> findByLastName(@Param("lastName") String lastName);
	
    /**
     * Finds super heros by his/her case-insensitive super hero name as a search criteria.
     * @param superHeroName  
     * @return  A list of super heros which super hero name is a case-insensitive match with the given name.
     *          If no persons is found, this method returns an empty list.
     */
    @Query("SELECT s FROM SuperHero s where LOWER(s.superHeroName) = LOWER(:superHeroName)") 
    public List<SuperHero> findBySuperHeroName(@Param("superHeroName") String superHeroName);

}
