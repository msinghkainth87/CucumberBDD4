Feature: Login

# Background: SSO into GWCC
#    Given I navigate to ClaimCenter cc login page
##    And I enter "su" into input field login.username
##    And I enter "gw" into input field login.password
#   #example of data read from yaml file - also implements fill element method based on yaml input
#    And I fill the login page with super_user data
#    And I click login button
#    Then I land on activities page
#    Then the element general.gwlogo is displayed
    #Then I click on element having id "TabBar:ClaimTab-btnInnerEl"


  Scenario: 1 New claim creation - long form flexible
    Given I enter "NewClaim" into input field general.goto_page
    And I press "TAB" key into input field general.goto_page
    And I press "ENTER" key into input field general.goto_page
    Then I land on fnolsearchorcreatepolicy page
    And I enter "1000000028" into input field fnolsearchorcreatepolicy.policy_number
    And I enter "10/13/2018" into input field fnolsearchorcreatepolicy.enter_loss_date
    And I click on element fnolsearchorcreatepolicy.search_policy
    And the element fnolsearchorcreatepolicy.next is enabled
    When I click on element fnolsearchorcreatepolicy.next
    Then I land on fnolbasicinformation page
    Then I double click on element fnolbasicinformation.reported_by_name
    And  I enter "MARK WILLIAMS" into input field fnolbasicinformation.reported_by_name
    Then I click on element fnolbasicinformation.involved_vehicle_checkbox
    When I click on element fnolbasicinformation.next
    Then I land on fnollossdetails page
    Then I double click on element fnollossdetails.loss_cause
    And I enter "Collision at Uncontrolled Intersection" into input field fnollossdetails.loss_cause
    Then I double click on element fnollossdetails.loss_location_type
    And I enter fnollossdetails.ma_loss_address into input field fnollossdetails.loss_location_type
    When I click on element fnollossdetails.next
    Then I land on fnolservices page
    When I click on element fnolservices.next
    Then I land on fnolassignment page
    When I click on element fnolassignment.finish
    Then I land on fnolclaimsaved page
    And the element fnolclaimsaved.view_created_claim is displayed


  Scenario: short-form BDD compliant - needs Happy path fill methods
    Given I search for "NewClaim" in the information bar
    Then I land on fnolsearchorcreatepolicy page
    And I fill the fnol search or create policy page and continue
    Then I land on fnolbasicinformation page
    And  I fill the fnol basic information page and continue
    Then I land on fnollossdetails page
    And I fill the fnol loss details page and continue
    Then I land on fnolservices page
    Then I fill the fnol services page and continue
    Then I land on fnolassignment page
    Then I fill the fnol assignment page and continue
    Then I land on fnolclaimsaved page
    And the element fnolclaimsaved.view_created_claim is displayed

  @ui
  Scenario: Test with Amazon
    Given I navigate to "http:\\amazon.com"
    Then I search for "iPad"







