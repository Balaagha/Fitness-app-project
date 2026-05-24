package org.betech.fitnes.navigation

import cafe.adriel.voyager.core.screen.Screen
import org.betech.fitnes.presentation.onboarding.splash.SplashScreen

/**
 * Entry point for the onboarding flow. The Voyager `Navigator` is rooted
 * with [start]; each Screen owns the navigation decisions to the next step.
 */
object OnboardingNavGraph {
    val start: Screen get() = SplashScreen()
}
