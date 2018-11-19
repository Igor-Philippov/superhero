Feature: API BDDs

  @FirstScenario
  Scenario: Receive single mission
    Given Mission rest endpoint is up
    When User gets one mission by id 1
    Then Returned JSON object is not null

  @LastScenario
  Scenario: Receive single superHero
    Given SuperHero rest endpoint is up
    When User gets one superHero by id 1
    Then Returned JSON object is not null