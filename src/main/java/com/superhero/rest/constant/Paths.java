package com.superhero.rest.constant;

public class Paths {

	public static final String OD = "demo";
	
    public static final String VERSION = "/api/1.0/";
	
    /** /api/1.0/superheros */
    public static final String SUPERHEROS = VERSION + "superheros";
    
    /** /api/1.0/superheros/{id} */
    public static final String SUPERHERO_BY_ID = SUPERHEROS + "/{id}";
    
    /** /api/1.0/superheros/names/superhero/{superHeroName} */
    public static final String SUPERHERO_BY_SUPERHERONAME = SUPERHEROS + "/names/superhero/{superHeroName}";
    
    /** /api/1.0/superheros/names/first/{firstName} */
    public static final String SUPERHEROS_BY_FIRSTNAME = SUPERHEROS + "/names/first/{firstName}";
    
    /** /api/1.0/superheros/names/last/{lastName} */
    public static final String SUPERHEROS_BY_LASTNAME = SUPERHEROS + "/names/last/{lastName}";
    
    /** /api/1.0/missions */
    public static final String MISSIONS = VERSION + "missions";
	
    /** /api/1.0/missions/{id} */
    public static final String MISSION_BY_ID = MISSIONS + "/{id}";
    
    /** /api/1.0/missions/name/{name} */
    public static final String MISSIONS_BY_NAME = MISSIONS + "/name/{name}";
    
    /** /api/1.0/missions/completed */
    public static final String MISSIONS_COMPLETED = MISSIONS + "/completed";
    
    /** /api/1.0/missions/deleted */
    public static final String MISSIONS_DELETED = MISSIONS + "/deleted";

}
