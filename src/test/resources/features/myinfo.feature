@MYINFO
Feature: OrangeHRM My Info Module

  @MYINFO_01
  Scenario: Open My Info page
    Given I am logged in and on My Info page

  @MYINFO_02
  Scenario: Update Personal Details
    Given I am logged in and on My Info page
    When I update personal details
    Then the personal details should be saved

  @MYINFO_03
  Scenario: Update Contact Details
    Given I am logged in and on My Info page
    When I update contact details
    Then the contact details should be saved
