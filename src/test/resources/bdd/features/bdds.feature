Feature: API BDDs

  @FirstScenario
  Scenario: Receive a single mission by valid ID
    Given Mission rest endpoint is up
    When User gets one mission by id 90
    Then The HTTP response status code is 200
    And Returned JSON object is not null
    
 # Scenario: client makes call to GET /version
 #   When the client calls /version
 #   Then the client receives status code of 200
 #   And the client receives server version 1.0

  @LastScenario
  Scenario: Receive single superHero
    Given SuperHero rest endpoint is up
    When User gets one superHero by id 1
    Then Returned JSON object is not null