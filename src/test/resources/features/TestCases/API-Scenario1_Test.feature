Feature: Swagger API Call

	Swagger API Call

	@API-Scenario1
	Scenario: Scenario1 - Verify login functionality
		Given User is on login page for TestCase "API-Scenario1"
		When Call API 1 "https://automationintesting.online/" request "GET" URL with following parameters
		 | endpoint | JSON |
		 | params | JSON |
		 | auth | JSON |
		 | headers | JSON |
		 | body | JSON |
		 | script | JSON |
		 | field | JSON |

		When Call API 2 "https://automationintesting.online/branding/" request "GET" URL with following parameters
		 | endpoint | JSON |
		 | params | JSON |
		 | auth | JSON |
		 | headers | JSON |
		 | body | JSON |
		 | script | JSON |
		 | field | JSON |

		When Call API 3 "https://automationintesting.online/room/" request "GET" URL with following parameters
		 | endpoint | JSON |
		 | params | JSON |
		 | auth | JSON |
		 | headers | JSON |
		 | body | JSON |
		 | script | JSON |
		 | field | JSON |

