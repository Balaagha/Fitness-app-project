package org.betech.fitnes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.betech.fitnes.designsystem.color.VoltTheme
import org.betech.fitnes.navigation.OnboardingNavGraph
import org.betech.fitnes.presentation.onboarding.languageselect.LanguageSelectScreen
import org.betech.fitnes.presentation.onboarding.login.LoginScreen
import org.betech.fitnes.presentation.onboarding.q1goal.Q1GoalScreen
import org.betech.fitnes.presentation.onboarding.q2sex.Q2SexScreen
import org.betech.fitnes.presentation.onboarding.q3age.Q3AgeScreen
import org.betech.fitnes.presentation.onboarding.q4heightweight.Q4HeightWeightScreen
import org.betech.fitnes.presentation.onboarding.q5experience.Q5ExperienceScreen
import org.betech.fitnes.presentation.onboarding.q6context.Q6ContextScreen
import org.betech.fitnes.presentation.onboarding.aidisclosure.AiDisclosureScreen
import org.betech.fitnes.presentation.onboarding.authgate.AuthGateScreen
import org.betech.fitnes.presentation.onboarding.emaillogin.EmailLoginScreen
import org.betech.fitnes.presentation.onboarding.emailsignup.EmailSignupScreen
import org.betech.fitnes.presentation.onboarding.emailverify.EmailVerifyScreen
import org.betech.fitnes.presentation.onboarding.home.HomeScreen
import org.betech.fitnes.presentation.onboarding.parentalbottomsheet.ParentalBottomSheetScreen
import org.betech.fitnes.presentation.onboarding.parentalnotice.ParentalNoticeScreen
import org.betech.fitnes.presentation.onboarding.paywall.PaywallScreen
import org.betech.fitnes.presentation.onboarding.pregnancyconfirm.PregnancyConfirmScreen
import org.betech.fitnes.presentation.onboarding.pregnancynudge.PregnancyNudgeScreen
import org.betech.fitnes.presentation.onboarding.profilesummary.ProfileSummaryScreen
import org.betech.fitnes.presentation.onboarding.pwdresetemail.PwdResetEmailScreen
import org.betech.fitnes.presentation.onboarding.pwdresetform.PwdResetFormScreen
import org.betech.fitnes.presentation.onboarding.q7daysession.Q7DaySessionScreen
import org.betech.fitnes.presentation.onboarding.deleteacc1.DeleteAcc1Screen
import org.betech.fitnes.presentation.onboarding.deleteacc2.DeleteAcc2Screen
import org.betech.fitnes.presentation.onboarding.signoutconfirm.SignoutConfirmScreen
import org.betech.fitnes.presentation.onboarding.splash.SplashScreen
import org.betech.fitnes.presentation.onboarding.safeplan.SafePlanScreen
import org.betech.fitnes.presentation.onboarding.todaysafeworkout.TodaySafeWorkoutScreen
import org.betech.fitnes.presentation.onboarding.exercisedetailpreg.ExerciseDetailPregScreen
import org.betech.fitnes.presentation.onboarding.settingspregmode.SettingsPregModeScreen
import org.betech.fitnes.presentation.onboarding.trimesterpostpartum.TrimesterPostpartumScreen
import org.betech.fitnes.presentation.onboarding.welcome.WelcomeScreen
import org.betech.fitnes.presentation.onboarding.loginerror.LoginErrorScreen
import org.betech.fitnes.presentation.onboarding.signupemailexists.SignupEmailExistsScreen
import org.betech.fitnes.presentation.onboarding.ratelimit.RateLimitScreen
import org.betech.fitnes.presentation.onboarding.offlinebanner.OfflineBannerScreen
import org.betech.fitnes.presentation.onboarding.q3agesoftwarning.Q3AgeSoftWarningScreen
import org.betech.fitnes.presentation.onboarding.resetlinkexpired.ResetLinkExpiredScreen

/**
 * Root entry composable shared between Android and iOS.
 * Wraps the navigation graph in the Volt-themed Material3 surface.
 *
 * @param devScreen Optional deep-link query parameter for jumping to a specific
 *   screen during development. Accepted values: `splash`, `languageselect`,
 *   `welcome`, `q1goal`, `q2sex`, `q3age`, `q4heightweight`, `q5experience`,
 *   `q6context`, `q7daysession`, `profilesummary`, `signoutconfirm`, `login`.
 *   Unrecognised → default Splash entry.
 *
 *   Android: `adb shell am start -W -a android.intent.action.VIEW \
 *     -d "fitnes://app?devScreen=welcome" org.betech.fitnes`
 */
@Composable
fun App(devScreen: String? = null) {
    VoltTheme {
        Navigator(resolveStartScreen(devScreen))
    }
}

private fun resolveStartScreen(devScreen: String?): Screen = when (devScreen?.lowercase()) {
    "splash" -> SplashScreen()
    "languageselect", "language-select", "lang" -> LanguageSelectScreen()
    "welcome", "welcome0", "welcome1", "welcome2", "welcome3", "welcome4" -> WelcomeScreen()
    "q1goal", "q1-goal", "q1" -> Q1GoalScreen()
    "q2sex", "q2-sex", "q2" -> Q2SexScreen()
    "q3age", "q3-age", "q3" -> Q3AgeScreen()
    "q4heightweight", "q4-heightweight", "q4" -> Q4HeightWeightScreen()
    "q5experience", "q5-experience", "q5" -> Q5ExperienceScreen()
    "q6context", "q6-context", "q6" -> Q6ContextScreen()
    "q7daysession", "q7-daysession", "q7" -> Q7DaySessionScreen()
    "profilesummary", "profile-summary", "summary" -> ProfileSummaryScreen()
    "aidisclosure", "ai-disclosure", "ai" -> AiDisclosureScreen()
    "authgate", "auth-gate", "auth" -> AuthGateScreen()
    "emailsignup", "email-signup", "signup" -> EmailSignupScreen()
    "emaillogin", "email-login" -> EmailLoginScreen()
    "emailverify", "email-verify", "verify" -> EmailVerifyScreen()
    "pwdresetemail", "pwd-reset", "reset" -> PwdResetEmailScreen()
    "pwdresetform", "pwd-reset-form", "resetform" -> PwdResetFormScreen()
    "paywall" -> PaywallScreen()
    "signoutconfirm", "signout-confirm", "signout" -> SignoutConfirmScreen()
    "deleteacc1", "delete-acc-1", "deleteacc-1" -> DeleteAcc1Screen()
    "deleteacc2", "delete-acc-2", "deleteacc-2" -> DeleteAcc2Screen()
    "pregnancynudge", "pregnancy-nudge", "pregnancy" -> PregnancyNudgeScreen()
    "pregnancyconfirm", "pregnancy-confirm" -> PregnancyConfirmScreen()
    "trimesterpostpartum", "trimester-postpartum", "trimester" -> TrimesterPostpartumScreen()
    "safeplan", "safe-plan" -> SafePlanScreen()
    "todaysafeworkout", "today-safe-workout", "todayworkout" -> TodaySafeWorkoutScreen()
    "exercisedetailpreg", "exercise-detail-preg", "exdetailpreg" -> ExerciseDetailPregScreen()
    "settingspregmode", "settings-preg-mode", "pregmode" -> SettingsPregModeScreen()
    "parentalnotice", "parental-notice", "parental", "privacypolicy", "privacy-policy" -> ParentalNoticeScreen()
    "parentalbottomsheet", "parental-bottomsheet", "parentalsheet", "privacysheet" -> ParentalBottomSheetScreen()
    "home" -> HomeScreen()
    "login" -> LoginScreen()
    // V-error variants (iter 32)
    "loginerror", "login-error" -> LoginErrorScreen()
    "signupemailexists", "signup-email-exists" -> SignupEmailExistsScreen()
    "ratelimit", "rate-limit" -> RateLimitScreen()
    "offlinebanner", "offline-banner" -> OfflineBannerScreen()
    "q3agesoftwarning", "q3-age-soft-warning", "q3soft" -> Q3AgeSoftWarningScreen()
    "resetlinkexpired", "reset-link-expired", "resetexpired" -> ResetLinkExpiredScreen()
    else -> OnboardingNavGraph.start
}
