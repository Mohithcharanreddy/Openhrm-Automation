@Login
Feature: OrangeHRM Login Functionality

  @Login
  Scenario: Login with valid username and password
    Given I am on the OrangeHRM login page
    When I enter valid username "admin" and password "admin123"
    And I click on the login button
    Then I should be logged in successfully

  @Login
  Scenario: Login with invalid username
    Given I am on the OrangeHRM login page
    When I enter invalid username "invalid" and password "admin123"
    And I click on the login button
    Then I should see an error message "Invalid credentials"

  @Login
  Scenario: Login with invalid password
    Given I am on the OrangeHRM login page
    When I enter valid username "admin" and invalid password "invalid"
    And I click on the login button
    Then I should see an error message "Invalid credentials"

  @Login
  Scenario: Login with blank credentials
    Given I am on the OrangeHRM login page
    When I click on the login button
    Then I should see an error message "Required"

  @Login
  Scenario: Verify all UI elements on Login page
    Given I am on the OrangeHRM login page
    Then the username field should be visible
    And the password field should be visible
    And the login button should be visible

  @Login
  Scenario: Verify error message for invalid login
    Given I am on the OrangeHRM login page
    When I enter invalid username "invalid" and invalid password "invalid"
    And I click on the login button
    Then I should see an error message "Invalid credentials"
