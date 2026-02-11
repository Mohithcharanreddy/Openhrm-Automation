@ForgotPassword
Feature: OrangeHRM Forgot Password

  @ForgotPassword
  Scenario: Verify forgot password link navigation
    Given I am on the OrangeHRM login page
    When I click on the forgot password link
    Then I should be navigated to the reset password page

  @ForgotPassword
  Scenario: Reset password with valid username
    Given I am on the forgot password page
    When I enter valid username "admin"
    And I click on the reset button
    Then I should see a success message "Reset Password link sent successfully"

  @ForgotPassword
  Scenario: Reset password with invalid username
    Given I am on the forgot password page
    When I enter invalid username "invalid"
    And I click on the reset button
    Then I should see a success message "Reset Password link sent successfully"

  @ForgotPassword
  Scenario: Verify success message after reset
    Given I am on the forgot password page
    When I enter valid username "admin"
    And I click on the reset button
    Then I should see a success message "Reset Password link sent successfully"
