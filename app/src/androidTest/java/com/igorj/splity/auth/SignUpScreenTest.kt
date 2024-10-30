package com.igorj.splity.auth

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.igorj.splity.R
import com.igorj.splity.model.auth.AuthNavigationScreen
import com.igorj.splity.ui.composable.auth.sign_in.SignInScreen
import com.igorj.splity.ui.composable.auth.sign_up.SignUpScreen
import com.igorj.splity.ui.theme.SplityTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signUpScreen_hasTopLabel() {
        var topLabelText = ""
        var topLabelTextTestTag = ""

        composeTestRule.setContent {
            val context = LocalContext.current

            topLabelText = context.getString(R.string.signUpScreen_ui_topLabel)
            topLabelTextTestTag = context.getString(R.string.signUpForm_ui_topLabel_testTag)
            SplityTheme {
                SignUpScreen(onSignUpClicked = { _ -> } )
            }
        }

        composeTestRule.onNodeWithTag(topLabelTextTestTag)
            .assertExists()
            .assertTextEquals(topLabelText)
    }

    @Test
    fun signUpScreen_usernameTextFieldRendersAndAcceptsInput() {
        var usernameLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            usernameLabelText = context.getString(R.string.signUpScreen_ui_usernameTextFieldLabel)
            SplityTheme {
                SignUpScreen(onSignUpClicked = { _ -> })
            }
        }

        composeTestRule.onNodeWithText(usernameLabelText).assertExists()
            .performTextInput("test_username")
    }

    @Test
    fun signUpScreen_emailTextFieldRendersAndAcceptsInput() {
        var emailLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            emailLabelText = context.getString(R.string.signUpScreen_ui_emailTextFieldLabel)
            SplityTheme {
                SignUpScreen(onSignUpClicked = { _ -> })
            }
        }

        composeTestRule.onNodeWithText(emailLabelText).assertExists()
            .performTextInput("test_email")
    }

    @Test
    fun signUpScreen_phoneNumberTextFieldRendersAndAcceptsInput() {
        var phoneNumberLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            phoneNumberLabelText = context.getString(
                R.string.signUpScreen_ui_phoneNumberTextFieldLabel
            )
            SplityTheme {
                SignUpScreen(onSignUpClicked = { _ -> })
            }
        }

        composeTestRule.onNodeWithText(phoneNumberLabelText).assertExists()
            .performTextInput("test_phone_number")
    }

    @Test
    fun signUpScreen_passwordTextFieldRendersAndAcceptsInput() {
        var passwordLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            passwordLabelText = context.getString(R.string.signUpScreen_ui_passwordTextFieldLabel)
            SplityTheme {
                SignInScreen(onSignInClicked = { _, _ -> })
            }
        }

        composeTestRule.onNodeWithText(passwordLabelText).assertExists()
            .performTextInput("test_password")
    }

    @Test
    fun signUpScreen_passwordFieldTogglesVisibilityIcon() {
        var passwordHintVisibleTestTag = ""
        var passwordVisibleDescriptionText = ""
        var passwordHiddenDescriptionText = ""

        composeTestRule.setContent {
            val context = LocalContext.current

            passwordHintVisibleTestTag = context.getString(
                R.string.textField_alt_showPasswordButton_testTag
            )
            passwordVisibleDescriptionText = context.getString(
                R.string.textField_alt_hidePasswordButton
            )
            passwordHiddenDescriptionText = context.getString(
                R.string.textField_alt_showPasswordButton
            )
            SplityTheme {
                SignInScreen(onSignInClicked = { _, _ -> })
            }
        }

        composeTestRule.onNodeWithTag(passwordHintVisibleTestTag)
            .assertExists()
            .assertContentDescriptionEquals(passwordHiddenDescriptionText)

        composeTestRule.onNodeWithTag(passwordHintVisibleTestTag).performClick()

        composeTestRule.onNodeWithTag(passwordHintVisibleTestTag)
            .assertContentDescriptionEquals(passwordVisibleDescriptionText)

        composeTestRule.onNodeWithTag(passwordHintVisibleTestTag).performClick()

        composeTestRule.onNodeWithTag(passwordHintVisibleTestTag)
            .assertContentDescriptionEquals(passwordHiddenDescriptionText)
    }

    @Test
    fun signUpScreen_repeatPasswordFieldRendersAndAcceptsInput() {
        var repeatPasswordLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            repeatPasswordLabelText = context.getString(
                R.string.signUpScreen_ui_repeatPasswordTextFieldLabel
            )
            SplityTheme {
                SignUpScreen(onSignUpClicked = { _ -> })
            }
        }

        composeTestRule.onNodeWithText(repeatPasswordLabelText).assertExists()
            .performTextInput("test_password")
    }

    @Test
    fun signUpScreen_confirmButtonWorks() {
        var confirmButtonText = ""
        var confirmButtonTestTag = ""

        composeTestRule.setContent {
            val context = LocalContext.current

            confirmButtonText = context.getString(R.string.signUpScreen_ui_confirmButton)
            confirmButtonTestTag = context.getString(
                R.string.authBottomOptions_ui_confirmButton_testTag
            )
            SplityTheme {
                SignUpScreen(onSignUpClicked = { _ -> })
            }
        }

        composeTestRule.onNodeWithTag(confirmButtonTestTag)
            .assertExists()
            .assertTextEquals(confirmButtonText)
            .performClick()
    }

    @Test
    fun signUpScreen_alternativeButtonNavigatesToSignIn() {
        var navigateCalled = false
        var alternativeFullText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            val alternativeText = context.getString(
                R.string.signUpScreen_ui_bottomAlternativeText
            )
            val alternativeHighlightText = context.getString(
                R.string.signUpScreen_ui_bottomAlternativeHighlightText
            )
            alternativeFullText = "$alternativeText $alternativeHighlightText"

            SplityTheme {
                SignUpScreen(
                    onSignUpClicked = { _ -> },
                    onNavigate = { screen ->
                        navigateCalled = screen == AuthNavigationScreen.SignIn.name
                    }
                )
            }
        }

        composeTestRule.onNodeWithText(alternativeFullText).assertExists().performClick()

        assert(navigateCalled)
    }
}
