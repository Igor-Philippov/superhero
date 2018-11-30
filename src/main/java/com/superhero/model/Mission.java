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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** An entity class which contains the information on a mission. */
@Entity(name = "Mission")
@Table(name = "shc_mission")
@JsonPropertyOrder({ "id", "name", "completed" })
@ApiModel(description = "A hypotetical mission assignable to a hypotetical super hero")
public class Mission implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    //@ApiModelProperty(notes = "The database generated ID of mission", position = 0)
	private Long id;
    
	//@NaturalId
	@Column(name = "name", nullable = false, length = 255, unique = false)
    //@ApiModelProperty(required = true, allowEmptyValue = false, notes = "The name of mission", position = 1)
	private String name;
	
	@Column(name = "is_completed", nullable = false, columnDefinition = "bit default 0")
	@Type(type = "numeric_boolean")
    //@ApiModelProperty(notes = "Indicates whether the mission is completed", position = 2)
	private boolean isCompleted;

	@Column(name = "is_deleted", nullable = false, columnDefinition = "bit default 0")
	@Type(type = "numeric_boolean")
    //@ApiModelProperty(hidden = true)
	private boolean isDeleted;

    @ManyToMany(mappedBy = "missions")
    @Where(clause="is_deleted = 0")
    @JsonBackReference	
    //@ApiModelProperty(hidden = true)
// SASHA
//	@ManyToMany(fetch = LAZY, cascade = { DETACH, MERGE, REFRESH, PERSIST }, targetEntity = SuperHero.class)
//	@JoinTable(name = "shc_super_hero_mission", joinColumns = @JoinColumn(name = "m_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "sh_id", referencedColumnName = "id"))
//	@Where(clause="is_deleted = 0")
//	@JsonBackReference
	private List<SuperHero> superHeros = new ArrayList<>();

    @JsonCreator
	public Mission() {
	}
	
//    @JsonCreator
//	public Mission(@JsonProperty("name") String name) {
//		this.name = name;
//	}
		
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

	@JsonIgnore
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
        return Objects.equals(id, other.id);
    }
	
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

//	@Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Mission other = (Mission) o;
//        return Objects.equals(name, other.name);
//    }
//	
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }
	
	@Override
	public String toString() {
		return String.format("com.superhero.model.Mission[id=%d, name='%s', isCompleted='%s', isDeleted='%s']",
				id, name, isCompleted, isDeleted);
	}
}