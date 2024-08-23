Feature: Login functionality

	Verify login functionality

	@UI-Scenario1
	Scenario: Scenario1 - Verify login functionality
		Given User is on login page for TestCase "UI-Scenario1"
		When Open page "https://automationintesting.online/"
		Then Verify Page Title is "Restful-booker-platform demo"
