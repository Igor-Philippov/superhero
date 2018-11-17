package com.superhero.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

/** An entity class which contains the information on a mission. */
@Entity
@Table(name = "shc_mission")
public class Mission implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    
	@NaturalId
	@Column(name = "name", nullable = false, length = 255, unique = true)
	private String name;
	
	@Column(name = "is_completed", nullable = false, columnDefinition = "bit default 0")
	@Type(type = "numeric_boolean")
	private boolean isCompleted;

	@Column(name = "is_deleted", nullable = false, columnDefinition = "bit default 0")
	@Type(type = "numeric_boolean")
	private boolean isDeleted;

    @ManyToMany(mappedBy = "missions")
    @Where(clause="is_deleted = 0")
    @JsonBackReference
	private List<SuperHero> superHeros = new ArrayList<>();

	public Mission() {
	}
	
	public Mission(String name) {
		this.name = name;
	}
		
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<SuperHero> getSuperHeros() {
		return superHeros;
	}

	public void setSuperHeros(List<SuperHero> superHeros) {
		this.superHeros = superHeros;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission other = (Mission) o;
        return Objects.equals(name, other.name);
    }
	
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
	
	@Override
	public String toString() {
		return String.format("com.superhero.model.Mission[id=%d, name='%s', isCompleted='%s', isDeleted='%s']",
				id, name, isCompleted, isDeleted);
	}
}