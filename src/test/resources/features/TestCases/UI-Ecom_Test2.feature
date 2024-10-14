Feature: Appium Testing

	Ecom App Login

	@UI-ecom
	Scenario: ecom - As a user of the Ecom mobile app I want to be able to log in
		Given User is on login page for TestCase "UI-ecom"
		Then User Verifies element "Logo" "text" having "id" "com.ecwid.android:id/fragment_onboarding_login_image_view_logo"
		Then User Verifies element "Slogan" "text" having "xpath" "//android.widget.TextView[@text='Start selling today']"
		Then User Verifies element "Create New Store" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_up"
		Then User Verifies element "Sign In" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_in"
		Then User Clicks element "Create New Store" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_up"
		Then User Verifies text of "Title" "Text" Should be "Create New Store" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_title"
		Then User Verifies element "Continue with Google" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_in_and_up_with_google"
		Then User Verifies element "Continue with Email" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_up_with_email"
		Then User Verifies element "Cross" "Icon" having "class" "android.widget.ImageView"
		Then User Clicks element "Continue with Email" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_up_with_email"
		Then User Verifies text of "Your Email Title" "Text" Should be "Your email" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_title"
		Then User Verifies text of "Page Text" "Text" Should be "Enter your business email address." having "xpath" "//android.widget.TextView[@text='Enter your business email address.']"
		Then User Verifies element "Email" "Text Field" having "id" "com.ecwid.android:id/valueET"
		Then User Verifies element is enabled "Next Step" "Button" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_continue"
		Then User Verifies text of "Next Step" "Button" Should be "Next Step" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_continue"
		Then User Enters value "testting@gmail.com" in  "Text Field" "Email" having "id" "com.ecwid.android:id/valueET"
		Then User Clicks element "Next Step" "Button" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_continue"
		Then User Verifies text of "Your Password Title" "Text" Should be "Your password" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_password_title"
		Then User Verifies text of "Page Text" "Text" Should be "Enter a password at least 6 characters long." having "xpath" "//android.widget.TextView[@text='Enter a password at least 6 characters long.']"
		Then User Verifies element "Show password" "Icon" having "accessibility id" "Show password"
		Then User Verifies element "Password" "Text Field" having "id" "com.ecwid.android:id/valueET"
		Then User Verifies element is enabled "Create Account" "Button" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_password_continue"
		Then User Verifies text of "Create Account" "Button" Should be "Create Account" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_password_continue"
		Then User Enters value "ssm##123" in  "Text Field" "Password" having "id" "com.ecwid.android:id/valueET"
		Then User Clicks element "Create Account" "Button" having "id" "com.ecwid.android:id/fragment_onboarding_create_email_password_continue"
		Then User Verifies text of "Alert Heading" "Text" Should be "We already have an account associated with this email" having "id" "com.ecwid.android:id/alertTitle"
		Then User Verifies text of "Alert Message" "Text" Should be "You can sign in into your existing account or create a new one using another email" having "id" "android:id/message"
		Then User Verifies element "Cancel" "Button" having "id" "android:id/button2"
		Then User Verifies element "Sign In" "Button" having "id" "android:id/button1"
		Then User Clicks element "Cancel" "Button" having "id" "android:id/button2"
		Then User Clicks back Button on Mobile
		Then User Clicks back Button on Mobile
		Then User Verifies element "Create New Store" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_up"
		Then User Verifies element "Sign In" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_in"
		Then User Clicks element "Sign In" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_login_button_sign_in"
		Then User Verifies text of "Page Heading" "Text" Should be "Sign In to Your Account" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_title"
		Then User Verifies element "Cross" "Icon" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_close"
		Then User Verifies element "Sign In with Google" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_in_and_up_with_google"
		Then User Verifies element "Sign In with QR Code" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_in_with_qr"
		Then User Verifies element "Sign In with Email" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_in_with_email"
		Then User Clicks element "Sign In with QR Code" "Anchor Link" having "id" "com.ecwid.android:id/fragment_onboarding_bottom_sheet_button_sign_in_with_qr"
		Then User Accepts alert
