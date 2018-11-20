package com.superhero.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "shc_super_hero")
public class SuperHero implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @NotEmpty
    @Column(name="name_first", nullable=false, length=32)
	private String firstName;

    @NotEmpty
    @Column(name="name_last", nullable=false, length=64)
	private String lastName;

    @NotEmpty
    @Column(name="name_superhero", nullable=false, length=128, unique=true)
	private String superHeroName;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        @JoinTable(name = "shc_super_hero_mission", 
	               joinColumns = @JoinColumn(name = "sh_id"),
	               inverseJoinColumns = @JoinColumn(name = "m_id"))
    @Where(clause="is_deleted = 0")
    @JsonManagedReference
	private List<Mission> missions = new ArrayList<>();
	
    @JsonCreator
	public SuperHero() {
	}
	
//    @JsonCreator
//	public SuperHero(@JsonProperty("firstname") String firstName,
//	                 @JsonProperty("lastname") String lastName, 
//	                 @JsonProperty("superheroname") String superHeroName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.superHeroName = superHeroName;
//    }
//    
//	@JsonCreator
//	public SuperHero(@JsonProperty("id") Long id, 
//			         @JsonProperty("firstname") String firstName,
//			         @JsonProperty("lastname") String lastName, 
//			         @JsonProperty("superheroname") String superHeroName,
//			         @JsonProperty("missions") List<Mission> missions) {
//		this(firstName, lastName, superHeroName);
//		this.id = id;
//		this.missions = missions;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSuperHeroName() {
		return superHeroName;
	}

	public void setSuperHeroName(String superHeroName) {
		this.superHeroName = superHeroName;
	}
	
	public List<Mission> getMissions() {
		return missions;
	}

	public void setMissions(List<Mission> missions) {
		this.missions = missions;
	}
	 
    public void addMission(Mission mission) {
        missions.add(mission);
        mission.getSuperHeros().add(this);
    }
 
    public void removeMission(Mission mission) {
        missions.remove(mission);
        mission.getSuperHeros().remove(this);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuperHero)) return false;
        return id != null && id.equals(((SuperHero) o).id);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	@Override
    public String toString() {
        return String.format(
                "com.superhero.model.SuperHero[id=%d, firstName='%s', lastName='%s', superHeroName='%s']",
                id, firstName, lastName, superHeroName);
    }
}