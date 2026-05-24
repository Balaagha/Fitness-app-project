package org.betech.fitnes.di

import kotlinx.serialization.json.Json
import org.betech.fitnes.data.repository.AnalyticsRepositoryImpl
import org.betech.fitnes.data.repository.AuthRepositoryImpl
import org.betech.fitnes.data.repository.OnboardingQuestionRepositoryImpl
import org.betech.fitnes.data.repository.OnboardingRepositoryImpl
import org.betech.fitnes.data.repository.UserProfileRepositoryImpl
import org.betech.fitnes.data.source.remote.RemoteSource
import org.betech.fitnes.data.source.remote.mock.MockRemoteSource
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.AuthRepository
import org.betech.fitnes.domain.repository.OnboardingQuestionRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.betech.fitnes.domain.repository.UserProfileRepository
import org.betech.fitnes.domain.usecase.CompleteOnboardingUseCase
import org.betech.fitnes.domain.usecase.EvaluatePregnancyHardStopUseCase
import org.betech.fitnes.domain.usecase.ResolvePersonaUseCase
import org.betech.fitnes.domain.usecase.SaveAnswerUseCase
import org.betech.fitnes.domain.usecase.StartOnboardingUseCase
import org.betech.fitnes.presentation.onboarding.languageselect.LanguageSelectViewModel
import org.betech.fitnes.presentation.onboarding.paywall.PaywallViewModel
import org.betech.fitnes.presentation.onboarding.q1goal.Q1GoalViewModel
import org.betech.fitnes.presentation.onboarding.q2sex.Q2SexViewModel
import org.betech.fitnes.presentation.onboarding.q3age.Q3AgeViewModel
import org.betech.fitnes.presentation.onboarding.q4heightweight.Q4HeightWeightViewModel
import org.betech.fitnes.presentation.onboarding.q5experience.Q5ExperienceViewModel
import org.betech.fitnes.presentation.onboarding.q6context.Q6ContextViewModel
import org.betech.fitnes.presentation.onboarding.aidisclosure.AiDisclosureViewModel
import org.betech.fitnes.presentation.onboarding.authgate.AuthGateViewModel
import org.betech.fitnes.presentation.onboarding.emaillogin.EmailLoginViewModel
import org.betech.fitnes.presentation.onboarding.emailsignup.EmailSignupViewModel
import org.betech.fitnes.presentation.onboarding.emailverify.EmailVerifyViewModel
import org.betech.fitnes.presentation.onboarding.pwdresetemail.PwdResetEmailViewModel
import org.betech.fitnes.presentation.onboarding.parentalbottomsheet.ParentalBottomSheetViewModel
import org.betech.fitnes.presentation.onboarding.parentalnotice.ParentalNoticeViewModel
import org.betech.fitnes.presentation.onboarding.pregnancyconfirm.PregnancyConfirmViewModel
import org.betech.fitnes.presentation.onboarding.pregnancynudge.PregnancyNudgeViewModel
import org.betech.fitnes.presentation.onboarding.safeplan.SafePlanViewModel
import org.betech.fitnes.presentation.onboarding.todaysafeworkout.TodaySafeWorkoutViewModel
import org.betech.fitnes.presentation.onboarding.exercisedetailpreg.ExerciseDetailPregViewModel
import org.betech.fitnes.presentation.onboarding.trimesterpostpartum.TrimesterPostpartumViewModel
import org.betech.fitnes.presentation.onboarding.pwdresetform.PwdResetFormViewModel
import org.betech.fitnes.presentation.onboarding.profilesummary.ProfileSummaryViewModel
import org.betech.fitnes.presentation.onboarding.q7daysession.Q7DaySessionViewModel
import org.betech.fitnes.presentation.onboarding.deleteacc1.DeleteAcc1ViewModel
import org.betech.fitnes.presentation.onboarding.deleteacc2.DeleteAcc2ViewModel
import org.betech.fitnes.presentation.onboarding.settingspregmode.SettingsPregModeViewModel
import org.betech.fitnes.presentation.onboarding.signoutconfirm.SignoutConfirmViewModel
import org.betech.fitnes.presentation.onboarding.splash.SplashViewModel
import org.betech.fitnes.presentation.onboarding.welcome.WelcomeViewModel
import org.betech.fitnes.presentation.onboarding.loginerror.LoginErrorViewModel
import org.betech.fitnes.presentation.onboarding.signupemailexists.SignupEmailExistsViewModel
import org.betech.fitnes.presentation.onboarding.ratelimit.RateLimitViewModel
import org.betech.fitnes.presentation.onboarding.offlinebanner.OfflineBannerViewModel
import org.betech.fitnes.presentation.onboarding.q3agesoftwarning.Q3AgeSoftWarningViewModel
import org.betech.fitnes.presentation.onboarding.resetlinkexpired.ResetLinkExpiredViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Data layer DI module — JSON, mock RemoteSource, repository bindings.
 * Swap [MockRemoteSource] → SupabaseRemoteSource later in one line.
 */
val dataModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            classDiscriminator = "type"
        }
    }
    single<RemoteSource> { MockRemoteSource(get()) }
    single<UserProfileRepository> { UserProfileRepositoryImpl(get(), get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), get()) }
    single<AnalyticsRepository> { AnalyticsRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<OnboardingQuestionRepository> { OnboardingQuestionRepositoryImpl(get(), get()) }
}

/**
 * Domain layer DI — use-cases. Factories (lightweight, stateless).
 */
val domainModule = module {
    factory { StartOnboardingUseCase(get(), get()) }
    factory { SaveAnswerUseCase(get(), get()) }
    factory { ResolvePersonaUseCase() }
    factory { CompleteOnboardingUseCase(get(), get()) }
    factory { EvaluatePregnancyHardStopUseCase() }
}

/**
 * Presentation layer DI — Orbit ViewModels exposed via `viewModel { … }`
 * so Compose call-sites can resolve them with `koinViewModel()`.
 */
val presentationModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LanguageSelectViewModel(get(), get()) }
    viewModel { WelcomeViewModel(get()) }
    viewModel { Q1GoalViewModel(get(), get()) }
    viewModel { Q2SexViewModel(get(), get()) }
    viewModel { Q3AgeViewModel(get(), get()) }
    viewModel { Q4HeightWeightViewModel(get(), get()) }
    viewModel { Q5ExperienceViewModel(get(), get()) }
    viewModel { Q6ContextViewModel(get(), get()) }
    viewModel { Q7DaySessionViewModel(get(), get()) }
    viewModel { ProfileSummaryViewModel(get(), get(), get()) }
    viewModel { AiDisclosureViewModel(get()) }
    viewModel { AuthGateViewModel(get(), get()) }
    viewModel { EmailSignupViewModel(get(), get()) }
    viewModel { EmailLoginViewModel(get(), get()) }
    viewModel { EmailVerifyViewModel() }
    viewModel { PwdResetEmailViewModel() }
    viewModel { PwdResetFormViewModel() }
    viewModel { PaywallViewModel(get()) }
    viewModel { SignoutConfirmViewModel(get()) }
    viewModel { DeleteAcc1ViewModel() }
    viewModel { DeleteAcc2ViewModel() }
    viewModel { PregnancyNudgeViewModel() }
    viewModel { PregnancyConfirmViewModel() }
    viewModel { TrimesterPostpartumViewModel(get(), get()) }
    viewModel { SafePlanViewModel() }
    viewModel { TodaySafeWorkoutViewModel() }
    viewModel { ExerciseDetailPregViewModel() }
    viewModel { SettingsPregModeViewModel() }
    viewModel { ParentalNoticeViewModel() }
    viewModel { ParentalBottomSheetViewModel() }

    // V-error variants (iter 32) — visual snapshots for QA review
    viewModel { LoginErrorViewModel() }
    viewModel { SignupEmailExistsViewModel() }
    viewModel { RateLimitViewModel() }
    viewModel { OfflineBannerViewModel() }
    viewModel { Q3AgeSoftWarningViewModel() }
    viewModel { ResetLinkExpiredViewModel() }
}
