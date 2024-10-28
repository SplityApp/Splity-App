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
import com.igorj.splity.ui.theme.SplityTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signInScreen_hasTopLabelWithCorrectText() {
        var topLabelText = ""
        var topLabelTextTestTag = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            topLabelText = context.getString(R.string.signInScreen_ui_topLabel)
            topLabelTextTestTag = context.getString(R.string.signInForm_ui_topLabel_testTag)

            SplityTheme {
                SignInScreen(onSignInClicked = { _, _ -> })
            }
        }

        composeTestRule.onNodeWithTag(topLabelTextTestTag)
            .assertExists()
            .assertTextEquals(topLabelText)
    }

    @Test
    fun signInScreen_emailTextFieldRendersAndAcceptsInput() {
        var emailLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            emailLabelText = context.getString(R.string.signInScreen_ui_emailTextFieldLabel)
            SplityTheme {
                SignInScreen(onSignInClicked = { _, _ -> })
            }
        }

        composeTestRule.onNodeWithText(emailLabelText).assertExists()
            .performTextInput("test@example.com")
    }

    @Test
    fun singInScreen_passwordTextFieldRendersAndAcceptsInput() {
        var passwordLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            passwordLabelText = context.getString(R.string.signInScreen_ui_passwordTextFieldLabel)
            SplityTheme {
                SignInScreen(onSignInClicked = { _, _ -> })
            }
        }

        composeTestRule.onNodeWithText(passwordLabelText).assertExists()
            .performTextInput("test_password")
    }

    @Test
    fun signInScreen_passwordFieldTogglesVisibilityIcon() {
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
    fun signInScreen_forgotPasswordTextButtonNavigatesToForgotPassword() {
        var forgotPasswordTextButtonText = ""
        var navigateCalled = false

        composeTestRule.setContent {
            val context = LocalContext.current
            forgotPasswordTextButtonText = context.getString(
                R.string.signInScreen_ui_forgotPasswordTextButton
            )
            SplityTheme {
                SignInScreen(
                    onSignInClicked = { _, _ -> },
                    onNavigate = { route ->
                        if (route == AuthNavigationScreen.ForgotPassword.name) {
                            navigateCalled = true
                        }
                    }
                )
            }
        }

        composeTestRule.onNodeWithText(forgotPasswordTextButtonText)
            .assertExists()
            .performClick()

        assert(navigateCalled) {
            "Navigation to ForgotPassword screen was not triggered."
        }
    }

    @Test
    fun signInScreen_confirmButtonWorks() {
        var confirmButtonText = ""
        var confirmButtonTestTag = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            confirmButtonText = context.getString(R.string.signInScreen_ui_confirmButton)
            confirmButtonTestTag = context.getString(
                R.string.authBottomOptions_ui_confirmButton_testTag
            )
            SplityTheme {
                SignInScreen(onSignInClicked = { _, _ -> })
            }
        }

        composeTestRule.onNodeWithTag(confirmButtonTestTag)
            .assertExists()
            .assertTextEquals(confirmButtonText)
            .performClick()
    }

    @Test
    fun signInScreen_alternativeButtonNavigatesToSignUp() {
        var navigateCalled = false
        var alternativeFullText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            val alternativeText = context.getString(R.string.signInScreen_ui_bottomAlternativeText)
            val alternativeHighlightText = context.getString(
                R.string.signInScreen_ui_bottomAlternativeHighlightText
            )
            alternativeFullText = "$alternativeText $alternativeHighlightText"

            SplityTheme {
                SignInScreen(
                    onSignInClicked = { _, _ -> },
                    onNavigate = { route ->
                        if (route == AuthNavigationScreen.SignUp.name) {
                            navigateCalled = true
                        }
                    }
                )
            }
        }

        composeTestRule.onNodeWithText(alternativeFullText)
            .assertExists()
            .performClick()

        assert(navigateCalled) {
            "Navigation to SignUp screen was not triggered."
        }
    }
}
