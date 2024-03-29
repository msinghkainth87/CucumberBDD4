@boc
Feature: Become a Seller Servicer
  @freddie
  Scenario: Test to verify Pre-app submission
    Given I navigate to singlefamily.landing page
    Then I should see page title as "Resources for Sellers/Servicers - Freddie Mac"
    #Then I run comparative accessibility tests using htmlcs,aXe tools
    And I click on element singlefamily.become_a_seller_servicer
    Then the element singlefamily.apply_to_be_seller_servicer is displayed
    And I click on element singlefamily.apply_to_be_seller_servicer
    Then the element singlefamily.submit_preapp is displayed
    And I click on element singlefamily.submit_preapp
    Then I should see page title as "Pre-Application Questionnaire"
    Then I run comparative accessibility tests using htmlcs,aXe tools
    Then I fill the preapp page with preapp_input data

  Scenario: Test to verify Pre-app submission
    Given I navigate to singlefamily.landing page
    Then I should see page title as "Resources for Sellers/Servicers - Freddie Mac"
    And I fill the singlefamily page with sf_input data
    Then I should see page title as "Pre-Application Questionnaire"
    Then I fill the preapp page with preapp_input data
    Then I run comparative accessibility tests using htmlcs,aXe tools