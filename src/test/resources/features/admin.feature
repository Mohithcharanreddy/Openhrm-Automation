@Admin
Feature: OrangeHRM Admin Functionality

  @Admin
  Scenario: Verify Admin page access after login
    Given I am logged in as admin
    When I click on the Admin menu
    Then I should see the Admin page

  @Admin
  Scenario: Search user by username
    Given I am logged in as admin
    When I click on the Admin menu
    When I search for user "admin"
    Then I should see the user in the search results

  @Admin
  Scenario: Add a new user with valid details
    Given I am logged in as admin
    When I click on the Admin menu
    When I click on Add button
    Then I fill in the user details with role "Admin" employee "Odis Adalwin" status "Enabled" username "newuser" password "password123"
    And I click on Save

  @Admin
  Scenario: Edit existing user details
    Given I am logged in as admin
    When I click on the Admin menu
    When I search for user "newuser"
    When I click on Edit button
    Then I update the user details
    And I click on Save

  @Admin
  Scenario: Delete an existing user
    Given I am logged in as admin
    When I click on the Admin menu
    When I search for user "newuser"
    When I click on Delete button
    Then the user should be deleted
