@LOGOUT
Feature: OrangeHRM Logout Module

  @LOGOUT_01
  Scenario: Logout from application
    Given I am logged in to OrangeHRM
    When I logout from the application
    Then I should be redirected to login page
