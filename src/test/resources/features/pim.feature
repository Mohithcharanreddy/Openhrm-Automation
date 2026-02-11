@PIM
Feature: OrangeHRM PIM Employee Management

  @PIM_01
  Scenario: Create employee and store id
    Given I am logged in and on PIM page
    When I add a new employee
    Then the employee should be created successfully

  @PIM_02
  Scenario: Search employee by stored id
    Given I am on PIM Employee List page
    When I search the employee by stored id
    Then the employee should appear in the results

  @PIM_03
  Scenario: Edit employee nickname
    Given I am on PIM Employee List page
    When I search the employee by stored id
    And I open the employee for edit
    And I update nickname
    Then the employee update should be saved

  @PIM_04
  Scenario: Upload employee profile picture
    Given I am on Employee Personal Details page
    When I upload a profile picture for the employee
    Then the profile picture should be uploaded successfully

  @PIM_05
  Scenario: Delete employee by stored id
    Given I am on PIM Employee List page
    When I delete the employee by stored id
    Then the employee should be deleted successfully
