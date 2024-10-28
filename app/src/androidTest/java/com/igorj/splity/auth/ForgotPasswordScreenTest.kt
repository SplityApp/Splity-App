package com.igorj.splity.auth

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.igorj.splity.R
import com.igorj.splity.model.auth.AuthNavigationScreen
import com.igorj.splity.ui.composable.auth.forgot_password.ForgotPasswordScreen
import com.igorj.splity.ui.theme.SplityTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgotPasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun forgotPasswordScreen_hasTopLabel() {
        var topLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            topLabelText = context.getString(R.string.forgotPasswordScreen_ui_topLabel)
            SplityTheme {
                ForgotPasswordScreen()
            }
        }

        composeTestRule.onNodeWithText(topLabelText).assertExists()
    }

    @Test
    fun forgotPasswordScreen_emailTextFieldRendersAndAcceptsInput() {
        var emailLabelText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            emailLabelText = context.getString(R.string.signInScreen_ui_emailTextFieldLabel)
            SplityTheme {
                ForgotPasswordScreen()
            }
        }

        composeTestRule.onNodeWithText(emailLabelText).assertExists()
            .performTextInput("test@example.com")
    }

    @Test
    fun forgotPasswordScreen_confirmButtonWorks() {
        var confirmButtonText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            confirmButtonText = context.getString(R.string.forgotPasswordScreen_ui_confirmButton)
            SplityTheme {
                ForgotPasswordScreen()
            }
        }

        composeTestRule.onNodeWithText(confirmButtonText).assertExists().performClick()
    }

    @Test
    fun forgotPasswordScreen_alternativeButtonNavigatesToSignIn() {
        var navigateCalled = false
        var alternativeFullText = ""

        composeTestRule.setContent {
            val context = LocalContext.current
            val alternativeText = context.getString(
                R.string.forgotPasswordScreen_ui_bottomAlternativeText
            )
            val alternativeHighlightText = context.getString(
                R.string.forgotPasswordScreen_ui_bottomAlternativeHighlightText
            )
            alternativeFullText = "$alternativeText $alternativeHighlightText"

            SplityTheme {
                ForgotPasswordScreen(onNavigate = { route ->
                    if (route == AuthNavigationScreen.SignIn.name) {
                        navigateCalled = true
                    }
                })
            }
        }

        composeTestRule.onNodeWithText(alternativeFullText).assertExists().performClick()

        assert(navigateCalled) {
            "Navigation to SignIn screen was not triggered."
        }
    }
}
