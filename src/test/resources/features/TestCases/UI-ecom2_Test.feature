Feature: Appium Testing

	Ecom App Login2

	@UI-ecom2
	Scenario: ecom2 - 2-As a user of the Ecom mobile app I want to be able to log in
		Given User is on login page for TestCase "UI-ecom2"
		Then User Verifies element "Logo" "text" having "id" "com.ecwid.android:id/fragment_onboarding_login_image_view_logo"
		Then User Verifies element "Slogan" "text" having "xpath" "//android.widget.TextView[@text='Start selling today']"
		Then User Verifies element "Create New Store" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_up"
		Then User Verifies element "Sign In" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_in"
		Then User Clicks element "Create New Store" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_up"
		Then User Verifies text of "Title" "Text" Should be "Create New Store" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_title"
		Then User Verifies element "Continue with Google" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_in_and_up_with_google"
		Then User Verifies element "Continue with Email" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_up_with_email"
		Then User Verifies element "Cross" "Icon" having "class" "android.widget.ImageView"
