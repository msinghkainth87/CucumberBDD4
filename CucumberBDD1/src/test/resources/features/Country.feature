Feature: Get Country

  Scenario: Get Country Name
    * url REST_HOST
    Given path 'US'
    When method GET
    Then status 200
    And match response.RestResponse.result.name == '#(country)'
