package org.betech.fitnes.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

/**
 * Minimal locale-aware string contract for Phase 1.
 * Properties grow as screens land in later phases.
 *
 * Doctrine (CLAUDE.md):
 *   - NEVER machine-translate. Each AZ/RU/EN value carries `// TODO: native review`.
 *   - "trainer" / "coach" / "personal coach" are FORBIDDEN copy terms —
 *     only "uyğunluq yoxlaması" / "mütəxəssis yoxlaması" allowed.
 *
 * Canonical AZ copy source:
 *   _bmad-output/planning-artifacts/ux-phase2-copy-bank-2026-05-23.md
 */
interface Strings {
    val appName: String

    // Splash
    val splashTagline: String

    // Action verbs
    val continueAction: String
    val backAction: String
    val skipAction: String
    val nextAction: String
    val doneAction: String

    // AI Disclosure (Apple 2025 requirement)
    val aiDisclosureBody: String

    // Medical hard-stop (pregnancy / postpartum)
    val medicalHardStopTitle: String
    val medicalHardStopBody: String

    // Generic errors
    val errorGeneric: String
    val errorNetwork: String

    // Language select (X2Pu6z)
    val langSelectTitle: String
    val langSelectNativeNotice: String
    val langOptionAz: String
    val langOptionRu: String
    val langOptionEn: String

    // Welcome (BPoym)
    val welcomeGreeting: String
    val welcomeTitleLine1: String
    val welcomeTitleLine2: String
    val welcomeSubtitle: String
    val welcomeChipWorkout: String
    val welcomeChipFood: String
    val welcomeChipEnergy: String
    val welcomeChipGoal: String
    // Iter 30: 5-variant rotation (variant 0 reuses welcomeTitleLine1/Line2/welcomeSubtitle).
    val welcomeV1Title: String
    val welcomeV1Subtitle: String
    val welcomeV2Title: String
    val welcomeV2Subtitle: String
    val welcomeV3Title: String
    val welcomeV3Subtitle: String
    val welcomeV4Title: String
    val welcomeV4Subtitle: String
    val welcomePillWorkout: String
    val welcomePillFood: String
    val welcomePillEnergy: String
    val welcomePillGoal: String
    val welcomeCtaStart: String
    val welcomeCtaHaveAccount: String
    val languagePillLabel: String

    // Q1 · Goal (Pencil S5QT23)
    val q1Title: String
    val q1Subtitle: String
    val q1OptionLoseFatTitle: String
    val q1OptionLoseFatSubtitle: String
    val q1OptionBuildMuscleTitle: String
    val q1OptionBuildMuscleSubtitle: String
    val q1OptionGetTonedTitle: String
    val q1OptionGetTonedSubtitle: String
    val q1OptionIncreaseStrengthTitle: String
    val q1OptionIncreaseStrengthSubtitle: String

    // Q2 · Sex (Pencil ObxuP)
    val q2Title: String
    val q2Subtitle: String
    val q2OptionMaleTitle: String
    val q2OptionMaleSubtitle: String
    val q2OptionFemaleTitle: String
    val q2OptionFemaleSubtitle: String
    val q2OptionPreferNotTitle: String
    val q2OptionPreferNotSubtitle: String

    // Q3 · Age (Pencil owS2i)
    val q3Title: String
    val q3Subtitle: String
    val q3AgeLabel: String
    val q3KeyboardHint: String
    val q3DialogTitle: String
    val q3DialogConfirm: String
    val q3DialogCancel: String

    // Q4 · Height + Weight (Pencil qXLw8)
    val q4Title: String
    val q4Subtitle: String
    val q4HeightCaption: String
    val q4WeightCaption: String
    val q4UnitCm: String
    val q4UnitFt: String
    val q4UnitIn: String
    val q4UnitKg: String
    val q4UnitLb: String
    val q4Hint: String
    val q4DialogHeightTitle: String
    val q4DialogWeightTitle: String

    // Q5 · Experience (Pencil i1Vu9)
    val q5Eyebrow: String
    val q5Title: String
    val q5Subtitle: String
    val q5OptionBeginnerTitle: String
    val q5OptionBeginnerSubtitle: String
    val q5OptionIntermediateTitle: String
    val q5OptionIntermediateSubtitle: String
    val q5OptionAdvancedTitle: String
    val q5OptionAdvancedSubtitle: String
    val q5OptionAthleteTitle: String
    val q5OptionAthleteSubtitle: String

    // Q6 · Context (Pencil F16e8)
    val q6Eyebrow: String
    val q6Title: String
    val q6Subtitle: String
    val q6OptionHomeBodyweightTitle: String
    val q6OptionHomeBodyweightSubtitle: String
    val q6OptionHomeEquipmentTitle: String
    val q6OptionHomeEquipmentSubtitle: String
    val q6OptionGymTitle: String
    val q6OptionGymSubtitle: String
    val q6OptionHybridTitle: String
    val q6OptionHybridSubtitle: String

    // Q7 · Day + Session (Pencil H0uZ0e)
    val q7Eyebrow: String
    val q7Title: String
    val q7Subtitle: String
    val q7DaysCaption: String
    val q7SessionCaption: String

    /**
     * Q7 summary chip text — dynamic, AZ canonical pattern:
     *   "Həftədə {days} məşq × {minutes} dəq seçildi"
     * Each locale provides its own templated formatting (no MT).
     */
    fun q7Summary(days: Int, minutes: Int): String

    // Common — final-step CTA shared with Q7 ("Bitir" / "Finish" / "Готово")
    val commonFinish: String

    // Profile Summary (Pencil u1cEVR · "12 · Profil Özeti")
    val profileSummaryEyebrow: String
    val profileSummaryTitle: String
    val profileRowGoal: String
    val profileRowSex: String
    val profileRowAge: String
    val profileRowHeightWeight: String
    val profileRowExperience: String
    val profileRowContext: String
    val profileRowSchedule: String
    val profileSummaryNotice: String
    val profileSummaryCtaCreate: String
    val profileSummaryCtaBack: String

    /** Placeholder shown for missing answer values (em-dash). Identical across locales. */
    val profileSummaryEmpty: String get() = "—"

    /** Height · Weight value template: "{h} sm · {w} kq" (locale unit symbols). */
    fun profileHeightWeightValue(heightCm: Int, weightKg: Double): String

    // AI Disclosure (Pencil eQcvv · "13 · AI Açıqlaması") — Apple 2025 mandatory.
    val aiDisclosureTitle: String
    val aiDisclosureSubtitle: String
    val aiDisclosureBullet1: String
    val aiDisclosureBullet2: String
    val aiDisclosureBullet3: String
    val aiDisclosureBullet4: String
    val aiDisclosureCtaConfirm: String
    val aiDisclosureCtaLearnMore: String
    val aiDisclosureLearnMoreToast: String

    // Parental Notice (Pencil XG54w · "07 · Parental Notice") — under-18 guardian consent.
    val parentalNoticeEyebrow: String
    val parentalNoticeTitle: String
    val parentalNoticeDisclaimer: String
    val parentalNoticeConsentLabel: String
    val parentalNoticeChipPrivacy: String
    val parentalNoticeChipTerms: String
    val parentalNoticeCtaContinue: String
    val parentalNoticeCtaBack: String

    // Parental Notice — Privacy Policy bottom-sheet (Pencil P5mDxB · "V7").
    val parentalSheetEyebrow: String
    val parentalSheetTitle: String
    val parentalSheetUpdatedLabel: String
    val parentalSheetLangLabel: String
    val parentalSheetSection1Title: String
    val parentalSheetSection1Body: String
    val parentalSheetSection2Title: String
    val parentalSheetSection2Body: String
    val parentalSheetSection3Title: String
    val parentalSheetSection3Body: String
    val parentalSheetSection4Title: String
    val parentalSheetSection4Body: String
    val parentalSheetFooterHint: String
    val parentalSheetCtaClose: String

    // Auth Gate (Pencil K1n7u5 · "14 · AuthGate")
    val authGateTitle: String
    val authGateSubtitle: String
    val authGateAppleCta: String
    val authGateGoogleCta: String
    val authGateOrDivider: String
    val authGateEmailCta: String
    val authGateSkipCta: String
    val authGateFooterPrefix: String
    val authGateFooterTerms: String
    val authGateFooterPrivacy: String
    val authGateFooterSeparator: String
    val legalTodoToast: String

    // Email Signup (Pencil ZJFFO · "15 · Email Signup")
    val emailSignupTitle: String
    val emailLabelCaption: String
    val emailPlaceholder: String
    val passwordLabel: String
    val passwordConfirmLabel: String
    val pwRuleLength: String
    val pwRuleUppercase: String
    val pwRuleDigit: String
    val pwRuleSpecial: String
    val emailSignupCta: String
    val emailSignupHaveAccount: String
    val emailSignupLoginLink: String

    // Email Login (Pencil O8lWVO · "16 · Email Login")
    val emailLoginTitle: String
    val emailLoginForgotPassword: String
    val emailLoginCta: String
    val emailLoginNewUserPrefix: String
    val emailLoginSignupLink: String

    // Email Verify (Pencil zREhj · "17 · Email Təsdiq")
    /** Dynamic title — "$email-ə kod göndərdik" (AZ) / RU/EN locale-specific. */
    fun emailVerifySentTo(email: String): String

    val emailVerifySubtitle: String

    /** Resend label with mm:ss countdown — "Kodu yenidən göndər (00:NN)" (AZ). */
    fun emailVerifyResendCountdown(secondsLeft: Int): String

    val emailVerifyResendReady: String
    val emailVerifyResendToast: String
    val emailVerifyWrongEmailPrefix: String
    val emailVerifyWrongEmailLink: String

    // Password Reset · Email (Pencil rzAPa · "18 · Şifrə Sıfırla — Email")
    val pwdResetEmailTitle: String
    val pwdResetEmailSubtitle: String
    val pwdResetEmailPlaceholder: String
    val pwdResetEmailInfo: String
    val pwdResetEmailCta: String

    // Password Reset · Form (Pencil vA9Tb · "19 · Şifrə Sıfırla — Form")
    val pwdResetFormTitle: String
    val pwdResetFormNewPasswordLabel: String
    val pwdResetFormConfirmLabel: String
    val pwdResetFormCta: String

    // Paywall (Pencil ij7jR · "22 · Paywall")
    val paywallEyebrow: String
    val paywallTitle: String
    val paywallFeature1: String
    val paywallFeature2: String
    val paywallFeature3: String
    val paywallFeature4: String
    val paywallFeature5: String
    val paywallTrialTitle: String
    val paywallTrialSubtitle: String
    val paywallAnnualTitle: String
    val paywallAnnualSubtitle: String
    val paywallRecommendedBadge: String
    val paywallTransparencyNotice: String
    val paywallSmallPrint: String
    val paywallCta: String
    val paywallRestore: String
    val paywallTerms: String
    val paywallPaymentApple: String
    val paywallPaymentGoogle: String
    val paywallPaymentLocal: String
    val paywallRestoreToast: String
    val paywallTermsToast: String

    // Signout Confirm (Pencil SCKUA · "23 · Çıxış Təsdiq")
    val signoutTitle: String
    val signoutSubtitle: String
    val signoutStatusBackup: String
    val signoutStatusOffline: String
    val signoutStatusSync: String
    val signoutSafetyNotice: String
    val signoutCtaConfirm: String
    val signoutCtaCancel: String

    // Delete Account — Step 1 (Pencil e74FR · "24 · Hesab Sil — Təsdiq 1")
    val deleteAcc1Title: String
    val deleteAcc1Subtitle: String
    val deleteAcc1Item1: String
    val deleteAcc1Item2: String
    val deleteAcc1Item3: String
    val deleteAcc1Item4: String
    val deleteAcc1ArchiveChip: String
    val deleteAcc1RecoveryNotice: String
    val deleteAcc1CtaCancel: String
    val deleteAcc1CtaContinue: String

    // Delete Account — Step 2 (Pencil GauGs · "25 · Hesab Sil — Təsdiq 2")
    val deleteAcc2Title: String
    val deleteAcc2Subtitle: String
    val deleteAcc2Caption: String
    val deleteAcc2Placeholder: String
    val deleteAcc2Helper: String
    val deleteAcc2RecoveryNotice: String
    val deleteAcc2CtaConfirm: String

    // Pregnancy Nudge (Pencil M52XdD · "20 · Hamiləlik Nudge")
    val pregnancyNudgeTitle: String
    val pregnancyNudgeSubtitle: String
    val pregnancyNudgeBenefit1: String
    val pregnancyNudgeBenefit2: String
    val pregnancyNudgeBenefit3: String
    val pregnancyNudgePrivacy: String
    val pregnancyNudgeCtaYes: String
    val pregnancyNudgeCtaNo: String

    // Pregnancy Confirm (Pencil pqupj · "21 · Hamiləlik Təsdiq")
    val pregnancyConfirmTopBarTitle: String
    val pregnancyConfirmEyebrow: String
    val pregnancyConfirmTitle: String
    val pregnancyConfirmSubtitle: String
    val pregnancyConfirmItem1Title: String
    val pregnancyConfirmItem1Subtitle: String
    val pregnancyConfirmItem2Title: String
    val pregnancyConfirmItem2Subtitle: String
    val pregnancyConfirmItem3Title: String
    val pregnancyConfirmItem3Subtitle: String
    val pregnancyConfirmItem4Title: String
    val pregnancyConfirmItem4Subtitle: String
    val pregnancyConfirmMedicalNote: String
    val pregnancyConfirmCta: String
    val pregnancyConfirmFootnote: String

    // Trimester / Postpartum (Pencil C6Ya4A · "22 · Trimester / Postpartum")
    val trimesterEyebrow: String
    val trimesterTitle: String
    val trimesterSubtitle: String
    val trimester1Title: String
    val trimester1Subtitle: String
    val trimester2Title: String
    val trimester2Subtitle: String
    val trimester3Title: String
    val trimester3Subtitle: String
    val trimesterPostpartumTitle: String
    val trimesterPostpartumSubtitle: String
    val trimesterFootnote: String

    // Safe 4-week Plan (Pencil o0BUd · "23 · Safe 4-week Plan") — curated static template.
    // NEVER triggers AI plan generation (pregnancy_postpartum=true hard-stop).
    val safePlanEyebrow: String
    val safePlanTitle: String
    val safePlanSubtitle: String
    val safePlanMetric1Value: String
    val safePlanMetric1Label: String
    val safePlanMetric2Value: String
    val safePlanMetric2Label: String
    val safePlanMetric3Value: String
    val safePlanMetric3Label: String
    val safePlanSectionThisWeek: String
    val safePlanWeekIndicator: String
    val safePlanDayMon: String
    val safePlanDayTue: String
    val safePlanDayWed: String
    val safePlanDayThu: String
    val safePlanDayFri: String
    val safePlanDaySat: String
    val safePlanDaySun: String
    val safePlanDayDose: String
    val safePlanFocusBanner: String
    val safePlanCtaToday: String
    val safePlanCtaInfo: String

    // Today's Safe Workout (Pencil QHsnW · "24 · Today's Safe Workout") — curated static template.
    // NEVER triggers AI plan/exercise generation (pregnancy_postpartum hard-stop).
    val todaySafeWorkoutTrimesterLabel: String
    val todaySafeWorkoutEyebrowToday: String
    val todaySafeWorkoutEyebrowIntensity: String
    val todaySafeWorkoutDateLabel: String
    val todaySafeWorkoutSessionTitle: String
    val todaySafeWorkoutChipDuration: String
    val todaySafeWorkoutChipCount: String
    val todaySafeWorkoutChipRpe: String
    val todaySafeWorkoutSectionExercises: String
    val todaySafeWorkoutBadgePregSafe: String
    val todaySafeWorkoutEx1Title: String
    val todaySafeWorkoutEx1Stats: String
    val todaySafeWorkoutEx2Title: String
    val todaySafeWorkoutEx2Stats: String
    val todaySafeWorkoutEx2Badge: String
    val todaySafeWorkoutEx3Title: String
    val todaySafeWorkoutEx3Stats: String
    val todaySafeWorkoutEx4Title: String
    val todaySafeWorkoutEx4Stats: String
    val todaySafeWorkoutEx5Title: String
    val todaySafeWorkoutEx5Stats: String
    val todaySafeWorkoutCtaStart: String
    val todaySafeWorkoutFooter: String

    // Exercise Detail (Pregnancy) (Pencil YZ38M · "25 · Exercise Detail (Pregnancy)").
    // Curated static template; NEVER triggers AI plan/exercise generation.
    val exerciseDetailPregSafeBadge: String
    val exerciseDetailPregMediaCaption: String
    val exerciseDetailPregAlternative: String
    val exerciseDetailPregTitle: String
    val exerciseDetailPregStat1: String
    val exerciseDetailPregStat2: String
    val exerciseDetailPregStat3: String
    val exerciseDetailPregModTitle: String
    val exerciseDetailPregModBody: String
    val exerciseDetailPregNotesTitle: String
    val exerciseDetailPregNotesCount: String
    val exerciseDetailPregNote1: String
    val exerciseDetailPregNote2: String
    val exerciseDetailPregNote3: String
    val exerciseDetailPregCtaDone: String
    val exerciseDetailPregSkip: String
    val exerciseDetailPregEasier: String

    // Settings · Pregnancy Mode (Pencil vUAuh · "29 · Hamiləlik rejimi tənzimi").
    // Curated static management surface; NEVER triggers AI plan generation.
    val settingsPregModeTitle: String
    val settingsPregModeStatusPill: String
    val settingsPregModeCardTitle: String
    val settingsPregModeStatPeriodLabel: String
    val settingsPregModeStatPeriodValue: String
    val settingsPregModeStatWeekLabel: String
    val settingsPregModeStatWeekValue: String
    val settingsPregModeStatStartLabel: String
    val settingsPregModeStatStartValue: String
    val settingsPregModeStatusSub: String
    val settingsPregModeSectionManage: String
    val settingsPregModeRowChangePeriodTitle: String
    val settingsPregModeRowChangePeriodSubtitle: String
    val settingsPregModeRowChangePeriodTrailing: String
    val settingsPregModeRowPostpartumTitle: String
    val settingsPregModeRowPostpartumSubtitle: String
    val settingsPregModeRowRemindersTitle: String
    val settingsPregModeRowRemindersSubtitle: String
    val settingsPregModeRowRemindersOn: String
    val settingsPregModeRowRemindersOff: String
    val settingsPregModeRowDoctorTitle: String
    val settingsPregModeRowDoctorSubtitle: String
    val settingsPregModeCtaDisable: String
    val settingsPregModeDisableFooter: String

    // V-error variants (iter 32) ────────────────────────────────────────────
    // V1 Login Error (Pencil u27ve)
    val loginErrorPwBadText: String
    val loginErrorWarning: String

    // V2 Signup Email Exists (Pencil cYfk5)
    val signupExistsEyebrow: String
    val signupExistsTitle: String
    val signupExistsEmailError: String
    val signupExistsGoLoginPill: String
    val signupExistsHelper: String
    val signupExistsPrivacyLink: String
    val signupExistsCta: String

    // V3 Rate Limit (Pencil IFSQ3)
    val rateLimitTitle: String
    val rateLimitBody: String
    val rateLimitCtaReset: String
    val rateLimitCtaDismiss: String
    fun rateLimitCountdown(mm: Int, ss: Int): String

    // V4 Offline Banner (Pencil K2TtZa)
    val offlineBannerText: String
    val offlineBannerRefresh: String
    val offlineFootnote: String

    // V5 Q3 Age Soft Warning (Pencil iNSs8)
    val q3SoftWarningText: String

    // V6 Reset Link Expired (Pencil a3Vwh6)
    val resetExpiredTitle: String
    val resetExpiredBody: String
    val resetExpiredCtaNew: String
    val resetExpiredCtaBack: String
}

/**
 * Type-to-confirm keyword for destructive Delete Account · Step 2 gate.
 * Locale-invariant on purpose: AZ keyword stays the schema sentinel across all
 * locales so the comparison stays exact and the danger-row UI stays predictable.
 */
const val deleteConfirmKeyword: String = "SİL"

// ──────────────────────────────────────────────────────────────────────────────
// AZ — canonical primary locale. Copy pulled from ux-phase2-copy-bank §AI Disclosure
// (eQcvv) and §V variants. All marked for native review.
// ──────────────────────────────────────────────────────────────────────────────
object StringsAz : Strings {
    override val appName: String = "FitLab" // brand mark — identical across locales

    override val splashTagline: String = "Elmə əsaslı tam fitness"
        // TODO: native review — splash tagline (AZ canonical)

    override val continueAction: String = "Davam et" // TODO: native review — onboarding CTA
    override val backAction: String = "Geri" // TODO: native review — generic nav
    override val skipAction: String = "İndi yox, sonra" // TODO: native review — pregnancy skip pattern
    override val nextAction: String = "Növbəti" // TODO: native review — generic step nav
    override val doneAction: String = "Bitir" // TODO: native review — final-step CTA (Q7 pattern)

    // Source: ux-phase2-copy-bank §AI Disclosure (eQcvv/Cs4v3/z2ZaHx) — kanonik, DƏYİŞDİRMƏ.
    override val aiDisclosureBody: String =
        "Plan AI tərəfindən elmi əsaslarla qurulur — son qərar səndədir, istədiyin vaxt dəyişə bilərsən. " +
            "Premium istifadəçilərdə plan, göndərilmədən əvvəl bir mütəxəssis tərəfindən yoxlanılıb onaylanır. " +
            "Bu tibbi məsləhət deyil — sağlamlıq probleminiz varsa məşqdən əvvəl həkimə müraciət edin."
            // TODO: native review — canonical AI Disclosure, requires legal sign-off

    override val medicalHardStopTitle: String = "Hamilə və ya yeni doğmuşsan?"
        // TODO: native review — Pregnancy (M52XdD)
    override val medicalHardStopBody: String =
        "Bu məlumatı bizə bildirsən, sənə uyğun, ehtiyatlı şablon hazırlayırıq. " +
            "Hər zaman Tənzimləmələrdə dəyişə bilərsən."
            // TODO: native review — medical hard-stop body

    override val errorGeneric: String = "Nə isə yanlış getdi" // TODO: native review — V2 error
    override val errorNetwork: String = "Bağlantı yoxdur — yenidən cəhd et"
        // TODO: native review — offline toast

    override val langSelectTitle: String = "Dilini seç"
        // TODO: native review — Language Select title (Pencil X2Pu6z)
    override val langSelectNativeNotice: String =
        "Bütün 3 dil ilə tərcümə edilib — maşın tərcüməsi yoxdur"
        // TODO: native review — native-translation moat copy (DO NOT MT)
    override val langOptionAz: String = "Azərbaycanca" // endonym — identical across locales
    override val langOptionRu: String = "Русский" // endonym — identical across locales
    override val langOptionEn: String = "English" // endonym — identical across locales

    // Welcome (Pencil BPoym) — canonical AZ copy.
    override val welcomeGreeting: String = "SALAM" // TODO: native review
    override val welcomeTitleLine1: String = "MƏŞQİNİ" // TODO: native review
    override val welcomeTitleLine2: String = "PLANLA" // TODO: native review
    override val welcomeSubtitle: String =
        "Super-set, drop-set, tempo — hər təkrar əlmlə qurulur."
        // TODO: native review — welcome subtitle
    override val welcomeChipWorkout: String = "məşqini" // TODO: native review
    override val welcomeChipFood: String = "qidanı" // TODO: native review (iter 30: aligns variant 2)
    override val welcomeChipEnergy: String = "enerjini" // TODO: native review (iter 30)
    override val welcomeChipGoal: String = "hədəflərini" // TODO: native review (iter 30)
    // Iter 30 — variant copy (AZ canonical).
    override val welcomeV1Title: String = "ÖZ MƏŞQİNİ İDARƏ ET"
    override val welcomeV1Subtitle: String =
        "Planla, izlə, ölç — hər addım sənin nəzarətində. Elmlə qurulur, AI ilə dəstəklənir."
    override val welcomeV2Title: String = "QİDANI İZLƏ"
    override val welcomeV2Subtitle: String =
        "Azərbaycan masası — düşbərə, dolma, plov. Şəkilini çək, kalori dərhal hazır."
    override val welcomeV3Title: String = "ENERJİNİ ÖLÇ"
    override val welcomeV3Subtitle: String =
        "Yandırdığın, yediyin, içdiyin hər yerdə. Balansını gərçək vaxtda gör."
    override val welcomeV4Title: String = "HƏDƏFƏ ÇAT"
    override val welcomeV4Subtitle: String =
        "Streak, milestone, plçü dəyişikliyi. Hər həftə bir addım yaxın."
    override val welcomePillWorkout: String = "Məşq" // TODO: native review
    override val welcomePillFood: String = "Qida" // TODO: native review
    override val welcomePillEnergy: String = "Enerji" // TODO: native review
    override val welcomePillGoal: String = "Hədəf" // TODO: native review
    override val welcomeCtaStart: String = "Başlayaq" // TODO: native review
    override val welcomeCtaHaveAccount: String = "Hesabım var" // TODO: native review
    override val languagePillLabel: String = "AZ" // brand-level locale tag

    // Q1 · Goal (Pencil S5QT23) — canonical AZ copy.
    override val q1Title: String = "Hədəfin nədir?" // TODO: native review
    override val q1Subtitle: String = "Planın bu seçimə görə qurulur" // TODO: native review
    override val q1OptionLoseFatTitle: String = "Çəki azaltmaq" // TODO: native review
    override val q1OptionLoseFatSubtitle: String = "Defisit · yağ itirmək" // TODO: native review
    override val q1OptionBuildMuscleTitle: String = "Əzələ qurmaq" // TODO: native review
    override val q1OptionBuildMuscleSubtitle: String = "Hipertrofiya · həcm artımı" // TODO: native review
    override val q1OptionGetTonedTitle: String = "Fit qalmaq" // TODO: native review
    override val q1OptionGetTonedSubtitle: String = "Balanslayıq · həftəlik aktivlik" // TODO: native review
    override val q1OptionIncreaseStrengthTitle: String = "Güc artırmaq" // TODO: native review
    override val q1OptionIncreaseStrengthSubtitle: String = "Compound liftlər · 1RM artımı" // TODO: native review

    // Q2 · Sex (Pencil ObxuP) — canonical AZ copy.
    override val q2Title: String = "Cinsin?" // TODO: native review
    override val q2Subtitle: String = "Kalori və protein hesabı bu sahaya bağlıdır" // TODO: native review
    override val q2OptionMaleTitle: String = "Kişi" // TODO: native review
    override val q2OptionMaleSubtitle: String = "Standart metabolik bazis" // TODO: native review
    override val q2OptionFemaleTitle: String = "Qadın" // TODO: native review
    override val q2OptionFemaleSubtitle: String = "Adaptiv kalori hesabı" // TODO: native review
    override val q2OptionPreferNotTitle: String = "Demək istəmirəm" // TODO: native review
    override val q2OptionPreferNotSubtitle: String = "Neytral cədvəl tətbiq olunur" // TODO: native review

    // Q3 · Age (Pencil owS2i) — canonical AZ copy.
    override val q3Title: String = "Yaşın neçədir?" // TODO: native review
    override val q3Subtitle: String = "Yaşına uyğun yüklənmə hesablanır" // TODO: native review
    override val q3AgeLabel: String = "YAŞ" // TODO: native review
    override val q3KeyboardHint: String = "Rəqəm klaviaturası açılır — toxun və yaz" // TODO: native review
    override val q3DialogTitle: String = "Yaşı daxil et" // TODO: native review
    override val q3DialogConfirm: String = "Tamam" // TODO: native review
    override val q3DialogCancel: String = "Ləğv et" // TODO: native review

    // Q4 · Height + Weight (Pencil qXLw8) — canonical AZ copy.
    override val q4Title: String = "Boyun və çəkin?" // TODO: native review
    override val q4Subtitle: String =
        "Kalori, protein və su hədəflərini hesablamaq üçün lazımdır"
        // TODO: native review
    override val q4HeightCaption: String = "BOY" // TODO: native review
    override val q4WeightCaption: String = "ÇƏKİ" // TODO: native review
    override val q4UnitCm: String = "cm" // unit symbol — identical across locales
    override val q4UnitFt: String = "ft" // unit symbol — identical across locales
    override val q4UnitIn: String = "in" // unit symbol — identical across locales
    override val q4UnitKg: String = "kq" // TODO: native review — AZ "kq" vs international "kg"
    override val q4UnitLb: String = "lb" // unit symbol — identical across locales
    override val q4Hint: String = "İstədiyin vaxt yenilə — həftəlik və ya aylıq"
        // TODO: native review
    override val q4DialogHeightTitle: String = "Boyu daxil et" // TODO: native review
    override val q4DialogWeightTitle: String = "Çəkini daxil et" // TODO: native review

    // Q5 · Experience (Pencil i1Vu9) — canonical AZ copy.
    override val q5Eyebrow: String = "5 / 7 · TƏCRÜBƏ" // TODO: native review
    override val q5Title: String = "Səviyyən necədir?" // TODO: native review
    override val q5Subtitle: String = "Səviyyən uyğun çətinlikdə başlayaq" // TODO: native review
    override val q5OptionBeginnerTitle: String = "Yeni başlayan" // TODO: native review
    override val q5OptionBeginnerSubtitle: String = "İlk 6 ay və ya az verib qayıtmışam" // TODO: native review
    override val q5OptionIntermediateTitle: String = "Orta səviyyə" // TODO: native review
    override val q5OptionIntermediateSubtitle: String = "6 ay – 2 il sabit məşq edirəm" // TODO: native review
    override val q5OptionAdvancedTitle: String = "Təcrübəli" // TODO: native review
    override val q5OptionAdvancedSubtitle: String = "2+ il, super-set və ileri texnikalarla" // TODO: native review
    override val q5OptionAthleteTitle: String = "Atlet" // TODO: native review
    override val q5OptionAthleteSubtitle: String = "Yarışmaya hazırlaşıram, peak phase" // TODO: native review

    // Q6 · Context (Pencil F16e8) — canonical AZ copy.
    override val q6Eyebrow: String = "6 / 7 · KONTEKST" // TODO: native review
    override val q6Title: String = "Harada məşq edirsən?" // TODO: native review
    override val q6Subtitle: String = "Hərəkət siyahısı və alternativləri bu məhdudlaşdır"
        // TODO: native review
    override val q6OptionHomeBodyweightTitle: String = "Evdə (avadanlıqsız)"
        // TODO: native review
    override val q6OptionHomeBodyweightSubtitle: String = "Yalnız bədən çəkisi və dəstək rejimi"
        // TODO: native review
    override val q6OptionHomeEquipmentTitle: String = "Evdə (avadanlıqlı)"
        // TODO: native review
    override val q6OptionHomeEquipmentSubtitle: String = "Dumbbell, kettlebell, bench"
        // TODO: native review
    override val q6OptionGymTitle: String = "Zalda" // TODO: native review
    override val q6OptionGymSubtitle: String = "Tam avadanlıq, çəki maşınları"
        // TODO: native review
    override val q6OptionHybridTitle: String = "Hibrid (ev + zal)" // TODO: native review
    override val q6OptionHybridSubtitle: String = "Həftəlik plan ikisini birləşdirir"
        // TODO: native review

    // Q7 · Day + Session (Pencil H0uZ0e) — canonical AZ copy.
    override val q7Eyebrow: String = "7 / 7 · CƏDVƏL" // TODO: native review
    override val q7Title: String = "Cədvəlin necə olsun?" // TODO: native review
    override val q7Subtitle: String = "Adaptiv plan həftədə bu qədər məşq quracaq"
        // TODO: native review
    override val q7DaysCaption: String = "HƏFTƏLİK GÜN" // TODO: native review
    override val q7SessionCaption: String = "SESSİYA MÜDDƏTİ (DƏQ)" // TODO: native review

    override fun q7Summary(days: Int, minutes: Int): String =
        "Həftədə $days məşq × $minutes dəq seçildi"
        // TODO: native review — Q7 summary chip (AZ canonical)

    override val commonFinish: String = "Bitir" // TODO: native review — Q7 final-step CTA

    // Profile Summary (Pencil u1cEVR) — canonical AZ copy.
    override val profileSummaryEyebrow: String = "PROFİL ÖZƏTİ"
        // TODO: native review — profile summary eyebrow (AZ canonical)
    override val profileSummaryTitle: String = "Hər şey doğrudurmu?"
        // TODO: native review — profile summary title
    override val profileRowGoal: String = "Hədəf" // TODO: native review
    override val profileRowSex: String = "Cinsiyyət" // TODO: native review
    override val profileRowAge: String = "Yaş" // TODO: native review
    override val profileRowHeightWeight: String = "Boy · Çəki" // TODO: native review
    override val profileRowExperience: String = "Təcrübə" // TODO: native review
    override val profileRowContext: String = "Kontekst" // TODO: native review
    override val profileRowSchedule: String = "Cədvəl" // TODO: native review
    override val profileSummaryNotice: String =
        "İstədiyin vaxt parametrləri tənzimləmələrdən yeniləyə bilərsən"
        // TODO: native review — profile summary disclaimer
    override val profileSummaryCtaCreate: String = "Profilimi yarat"
        // TODO: native review — profile summary primary CTA
    override val profileSummaryCtaBack: String = "Geriyə dön"
        // TODO: native review — profile summary secondary CTA

    override fun profileHeightWeightValue(heightCm: Int, weightKg: Double): String {
        val w = if (weightKg % 1.0 == 0.0) weightKg.toInt().toString() else weightKg.toString()
        return "$heightCm sm · $w kq"
        // TODO: native review — AZ uses "sm" (boy) + "kq" (çəki); confirm shorthand
    }

    // AI Disclosure (Pencil eQcvv) — canonical AZ. Apple 2025 mandatory disclosure copy.
    // Doctrine: user agency framing; "mütəxəssis uyğunluq yoxlaması" (NOT trainer/coach).
    override val aiDisclosureTitle: String = "Sənin planın AI ilə qurulur"
        // TODO: native review — AI Disclosure title (Apple submission item)
    override val aiDisclosureSubtitle: String =
        "Hər plan elmi qaydalar + adaptiv AI ilə hazırlanır. Aşağıdakılar həmişə doğru qalır:"
        // TODO: native review — AI Disclosure subtitle
    override val aiDisclosureBullet1: String =
        "Plan adaptasiyası daxili qaydalar + AI ilə qurulur."
        // TODO: native review
    override val aiDisclosureBullet2: String =
        "Son söz sənindir — istənilən hərəkəti və ya günü dəyişə bilərsən."
        // TODO: native review — user-agency bullet
    override val aiDisclosureBullet3: String =
        "Məlumatların sənin nəzarətindədir, istədiyin vaxt silə bilərsən."
        // TODO: native review — data control bullet
    override val aiDisclosureBullet4: String =
        "Mütəxəssis uyğunluq yoxlaması Faza 2-də açılacaq (premium)."
        // TODO: native review — Phase-2 expert review hint (NO "trainer/coach")
    override val aiDisclosureCtaConfirm: String = "Anladım, davam et"
        // TODO: native review — primary CTA
    override val aiDisclosureCtaLearnMore: String = "AI haqqında ətraflı"
        // TODO: native review — secondary CTA
    override val aiDisclosureLearnMoreToast: String = "Tezliklə açılacaq"
        // TODO: native review — placeholder until AI details page lands

    // Parental Notice (Pencil XG54w) — canonical AZ copy.
    override val parentalNoticeEyebrow: String = "İCAZƏ TƏLƏB OLUNUR"
        // TODO: native review
    override val parentalNoticeTitle: String = "Validəynin xəbəri varmı?"
        // TODO: native review
    override val parentalNoticeDisclaimer: String =
        "18 yaşdan kiçiksənsə, davam etmək üçün validəyn və ya qəyyumun icazəsi vacibdir. " +
            "Apple və Google sağlamlıq tətbiqləri qaydaları belə tələb edir."
        // TODO: native review
    override val parentalNoticeConsentLabel: String =
        "Validəynim/qəyyumum bu app-i istifadə etməyə razıdır"
        // TODO: native review
    override val parentalNoticeChipPrivacy: String = "Məxfilik siyasəti"
        // TODO: native review
    override val parentalNoticeChipTerms: String = "İstifadə şərtləri"
        // TODO: native review
    override val parentalNoticeCtaContinue: String = "Anladım, davam et"
        // TODO: native review
    override val parentalNoticeCtaBack: String = "Geri"
        // TODO: native review

    // Parental Notice — Privacy bottom-sheet (Pencil P5mDxB).
    override val parentalSheetEyebrow: String = "İCAZƏ TƏLƏB OLUNUR"
        // TODO: native review
    override val parentalSheetTitle: String = "Məxfilik siyasəti"
        // TODO: native review
    override val parentalSheetUpdatedLabel: String = "Yenilənib: 21 May 2026"
        // TODO: native review
    override val parentalSheetLangLabel: String = "AZ"
    override val parentalSheetSection1Title: String = "1. Topladığımız məlumat"
        // TODO: native review
    override val parentalSheetSection1Body: String =
        "Hesab yaradıqda email və əsas profil məlumatın (yaş, boy, çəki, hədəf) toplanır. " +
            "Bütün məşq və qida qeydlərin cihazında və şifrəli serverdə saxlanır."
        // TODO: native review
    override val parentalSheetSection2Title: String = "2. Necə istifadə edirik"
        // TODO: native review
    override val parentalSheetSection2Body: String =
        "Məlumatın yalnız sənə uyğun məşq planı hazırlamaq, irəliləyişini izləmək və " +
            "tətbiqi yaxşılaşdırmaq üçün istifadə olunur. Üçüncü tərəflərə satılmır."
        // TODO: native review
    override val parentalSheetSection3Title: String = "3. Hüquqların"
        // TODO: native review
    override val parentalSheetSection3Body: String =
        "İstənilən vaxt məlumatını silə bilərsən (Tənzimləmələr > Hesabı sil). " +
            "30 gün ərzində bərpa mümkündür, sonra tam silinir."
        // TODO: native review
    override val parentalSheetSection4Title: String = "4. Əlaqə"
        // TODO: native review
    override val parentalSheetSection4Body: String =
        "Sual və ya şikayət üçün: privacy@fitnessapp.az ünvanına yaz. " +
            "7 gün ərzində cavablandırırıq."
        // TODO: native review
    override val parentalSheetFooterHint: String = "Tam mətni oxumaq üçün sürüşdür"
        // TODO: native review
    override val parentalSheetCtaClose: String = "Anladım"
        // TODO: native review

    // Auth Gate (Pencil K1n7u5) — canonical AZ copy.
    override val authGateTitle: String = "Hesabını yarat"
        // TODO: native review — AuthGate title (AZ canonical)
    override val authGateSubtitle: String =
        "İrəliləyişin buludda təhlükəsiz qalsın — yeni cihazda da əldə edə bilərsən."
        // TODO: native review — AuthGate subtitle
    override val authGateAppleCta: String = "Apple ilə davam et"
        // TODO: native review — Apple SSO CTA
    override val authGateGoogleCta: String = "Google ilə davam et"
        // TODO: native review — Google SSO CTA
    override val authGateOrDivider: String = "və ya"
        // TODO: native review — divider text
    override val authGateEmailCta: String = "Email ilə davam et"
        // TODO: native review — email path CTA
    override val authGateSkipCta: String = "Onsuz davam et (məhdud rejim)"
        // TODO: native review — guest skip link
    override val authGateFooterPrefix: String = "Davam edərək qəbul edirsən: "
        // TODO: native review — legal footer prefix
    override val authGateFooterTerms: String = "Şərtlər"
        // TODO: native review — legal footer link (Terms)
    override val authGateFooterPrivacy: String = "Məxfilik Siyasəti"
        // TODO: native review — legal footer link (Privacy)
    override val authGateFooterSeparator: String = " · "
        // separator — identical across locales
    override val legalTodoToast: String = "TODO: hüquqi səhifə açılacaq"
        // TODO: native review — placeholder until legal pages land

    // Email Signup (Pencil ZJFFO) — canonical AZ copy.
    override val emailSignupTitle: String = "Email ilə qeydiyyat"
        // TODO: native review — Email Signup title (AZ canonical)
    override val emailLabelCaption: String = "E-poçt" // TODO: native review
    override val emailPlaceholder: String = "ad@numune.az" // TODO: native review
    override val passwordLabel: String = "Parol" // TODO: native review
    override val passwordConfirmLabel: String = "Parolu təkrarla" // TODO: native review
    override val pwRuleLength: String = "8+ simvol" // TODO: native review
    override val pwRuleUppercase: String = "Böyük hərf" // TODO: native review
    override val pwRuleDigit: String = "Rəqəm" // TODO: native review
    override val pwRuleSpecial: String = "Xüsusi simvol" // TODO: native review
    override val emailSignupCta: String = "Qeydiyyatdan keç" // TODO: native review
    override val emailSignupHaveAccount: String = "Hesabım var. " // TODO: native review
    override val emailSignupLoginLink: String = "Daxil ol" // TODO: native review

    // Email Login (Pencil O8lWVO) — canonical AZ copy.
    override val emailLoginTitle: String = "Daxil ol" // TODO: native review
    override val emailLoginForgotPassword: String = "Şifrəni unutdum?" // TODO: native review
    override val emailLoginCta: String = "Daxil ol" // TODO: native review
    override val emailLoginNewUserPrefix: String = "Yeni istifadəçi? " // TODO: native review
    override val emailLoginSignupLink: String = "Qeydiyyat" // TODO: native review

    // Email Verify (Pencil zREhj) — canonical AZ copy.
    override fun emailVerifySentTo(email: String): String =
        "$email-ə kod göndərdik"
        // TODO: native review — AZ canonical "sent to {email}" copy

    override val emailVerifySubtitle: String = "6 rəqəmli təsdiq kodunu daxil et."
        // TODO: native review

    override fun emailVerifyResendCountdown(secondsLeft: Int): String {
        val mm = (secondsLeft / 60).toString().padStart(2, '0')
        val ss = (secondsLeft % 60).toString().padStart(2, '0')
        return "Kodu yenidən göndər ($mm:$ss)"
        // TODO: native review — AZ resend countdown template
    }

    override val emailVerifyResendReady: String = "Kodu yenidən göndər"
        // TODO: native review
    override val emailVerifyResendToast: String = "Kod göndərildi" // TODO: native review
    override val emailVerifyWrongEmailPrefix: String = "Yanlış e-poçt? "
        // TODO: native review
    override val emailVerifyWrongEmailLink: String = "Dəyişdir" // TODO: native review

    // Password Reset · Email (Pencil rzAPa) — canonical AZ copy.
    override val pwdResetEmailTitle: String = "Şifrəni sıfırla"
        // TODO: native review — pwd reset email title
    override val pwdResetEmailSubtitle: String =
        "Email ünvanını gir — sənə bərpa linki göndərək"
        // TODO: native review
    override val pwdResetEmailPlaceholder: String = "email@nümunə.az"
        // TODO: native review
    override val pwdResetEmailInfo: String = "Link 15 dəqiqə ərzində etibarlıdır."
        // TODO: native review
    override val pwdResetEmailCta: String = "Linki göndər"
        // TODO: native review

    // Password Reset · Form (Pencil vA9Tb) — canonical AZ copy.
    override val pwdResetFormTitle: String = "Yeni şifrə qur"
        // TODO: native review
    override val pwdResetFormNewPasswordLabel: String = "Yeni parol"
        // TODO: native review
    override val pwdResetFormConfirmLabel: String = "Parolu təsdiqlə"
        // TODO: native review
    override val pwdResetFormCta: String = "Parolu yenilə"
        // TODO: native review

    // Paywall (Pencil ij7jR) — canonical AZ copy.
    override val paywallEyebrow: String = "PREMIUM"
        // TODO: native review
    override val paywallTitle: String = "Daha dərinə getməyə hazırsan?"
        // TODO: native review
    override val paywallFeature1: String = "100+ elmlə qurulmuş hərəkət (GIF + MP4)"
        // TODO: native review
    override val paywallFeature2: String = "Limitsiz AI plan + həftəlik adaptasiya"
        // TODO: native review
    override val paywallFeature3: String = "Mütəxəssis uyğunluq yoxlaması"
        // TODO: native review
    override val paywallFeature4: String = "Limitsiz foto-kalori (AZ Top-200)"
        // TODO: native review
    override val paywallFeature5: String = "Streak Freeze · Ramazan rejimi"
        // TODO: native review
    override val paywallTrialTitle: String = "7 gün pulsuz sına"
        // TODO: native review
    override val paywallTrialSubtitle: String = "Sonra 8 AZN/ay — istənilən vaxt ləğv et"
        // TODO: native review
    override val paywallAnnualTitle: String = "İllik plan — 60 AZN"
        // TODO: native review
    override val paywallAnnualSubtitle: String = "5 AZN/ay · 38% endirim"
        // TODO: native review
    override val paywallRecommendedBadge: String = "TÖVSİYƏ"
        // TODO: native review
    override val paywallTransparencyNotice: String =
        "Şəffaf ödəniş — tək tapla ləğv et, gizli ödəniş yoxdur."
        // TODO: native review
    override val paywallSmallPrint: String =
        "AI təminatçı qaydaları · Avtomatik yenilənmə açıq olacaq"
        // TODO: native review
    override val paywallCta: String = "Davam et"
        // TODO: native review
    override val paywallRestore: String = "Bərpa et"
        // TODO: native review
    override val paywallTerms: String = "Şərtlər"
        // TODO: native review
    override val paywallPaymentApple: String = "Apple Pay"
    override val paywallPaymentGoogle: String = "Google Pay"
    override val paywallPaymentLocal: String = "m10 / Pulpal"
    override val paywallRestoreToast: String = "TODO: bərpa et"
    override val paywallTermsToast: String = "TODO: şərtlər"

    // Signout Confirm (Pencil SCKUA) — canonical AZ copy.
    override val signoutTitle: String = "Çıxışa hazırsan?"
        // TODO: native review
    override val signoutSubtitle: String =
        "Profilin və 30 günlük offline məlumatın buludda qalır. Növbəti girişdə avtomatik geri qayıdacaq."
        // TODO: native review
    override val signoutStatusBackup: String = "Bulud yedəyi aktiv"
        // TODO: native review
    override val signoutStatusOffline: String = "Offline məlumatın 30 gün gözləyir"
        // TODO: native review
    override val signoutStatusSync: String = "Növbəti girişdə avtomatik sync"
        // TODO: native review
    override val signoutSafetyNotice: String =
        "Cihazdan tam silinsən belə, profil və premium status saxlanır."
        // TODO: native review
    override val signoutCtaConfirm: String = "Çıxış et"
        // TODO: native review
    override val signoutCtaCancel: String = "İmtina et"
        // TODO: native review

    // Delete Account · Step 1 (Pencil e74FR) — canonical AZ.
    override val deleteAcc1Title: String = "Hesabı silməyə hazırsanmı?"
        // TODO: native review
    override val deleteAcc1Subtitle: String =
        "Aşağıdakı məlumatlar 30 gün ərzində soft-archive olunur. " +
            "Bu müddətdə geri qayıtsanız, hər şey bərpa olunur."
        // TODO: native review
    override val deleteAcc1Item1: String = "100+ məşq və set tarixçəsi"
        // TODO: native review
    override val deleteAcc1Item2: String = "Ölçü və çəki dinamikası"
        // TODO: native review
    override val deleteAcc1Item3: String = "İrəliləyiş fotoları"
        // TODO: native review
    override val deleteAcc1Item4: String = "Profil və preferanslar"
        // TODO: native review
    override val deleteAcc1ArchiveChip: String = "arxiv"
        // TODO: native review
    override val deleteAcc1RecoveryNotice: String =
        "30 gün ərzində qayıtsan — bütün məlumatın bərpa olunur. " +
            "30 gündən sonra tamamilə silinir."
        // TODO: native review
    override val deleteAcc1CtaCancel: String = "İmtina et"
        // TODO: native review
    override val deleteAcc1CtaContinue: String = "Davam et"
        // TODO: native review

    // Delete Account · Step 2 (Pencil GauGs) — canonical AZ.
    override val deleteAcc2Title: String = "Son təsdiq"
        // TODO: native review
    override val deleteAcc2Subtitle: String =
        "Aşağıdakı sahaya SİL yaz ki, hesabı qəti silə bilək"
        // TODO: native review
    override val deleteAcc2Caption: String = "Təsdiq sözü"
        // TODO: native review
    override val deleteAcc2Placeholder: String = "SİL"
        // keyword — locale-invariant, do not translate
    override val deleteAcc2Helper: String =
        "Diqqət: 'l' böyük hərfdə (məcburi) yazılmalıdır"
        // TODO: native review
    override val deleteAcc2RecoveryNotice: String =
        "30 gün ərzində geri qayıtsan, məlumat bərpa olunur."
        // TODO: native review
    override val deleteAcc2CtaConfirm: String = "Hesabı sil"
        // TODO: native review

    // Pregnancy Nudge (Pencil M52XdD) — canonical AZ. Data-collection nudge, NOT a plan trigger.
    // Copy doctrine: NO medical claims, NO "trainer/coach"; uses "həkim icazəsi tövsiyə".
    override val pregnancyNudgeTitle: String = "Hamilə və ya yeni doğmuşan?"
        // TODO: native review — pregnancy nudge title (canonical AZ)
    override val pregnancyNudgeSubtitle: String =
        "Bu məlumat təhlükəsiz şablon hazırlamağımıza imkan verir. " +
            "Tənzimləmələrdə hər vaxt dəyişə bilərsən."
        // TODO: native review — pregnancy nudge subtitle
    override val pregnancyNudgeBenefit1: String = "Daha mühafizəkar yüklənmə"
        // TODO: native review
    override val pregnancyNudgeBenefit2: String = "Risk hərəkətləri avtomatik çıxarılır"
        // TODO: native review
    override val pregnancyNudgeBenefit3: String = "Həkim icazəsi tövsiyə olunur"
        // TODO: native review — wording: tövsiyə (NOT medical advice)
    override val pregnancyNudgePrivacy: String =
        "Bu məlumat şifrəli saxlanır və yalnız sənin plan adaptasiyan üçün istifadə olunur."
        // TODO: native review — privacy line
    override val pregnancyNudgeCtaYes: String = "Bəli, bildir"
        // TODO: native review — primary CTA
    override val pregnancyNudgeCtaNo: String = "İndi yox"
        // TODO: native review — ghost CTA

    // Pregnancy Confirm (Pencil pqupj) — canonical AZ. Safe-template branch entry.
    // Copy doctrine: NO AI plan trigger, NO medical claim; "həkim/mütəxəssis" wording only.
    override val pregnancyConfirmTopBarTitle: String = "Təhlükəsiz plan"
        // TODO: native review
    override val pregnancyConfirmEyebrow: String = "XÜSUSİ DÖVR"
        // TODO: native review
    override val pregnancyConfirmTitle: String =
        "Sənin və körpənin sağlamlığı önəmlidir"
        // TODO: native review
    override val pregnancyConfirmSubtitle: String =
        "Bu dövrdə standart məşq yerinə təhlükəsiz, yumşaq hərəkətlər təklif edirik. " +
            "Hər zaman həkim/məsləhət birinci yerdə olmalıdır."
        // TODO: native review
    override val pregnancyConfirmItem1Title: String = "Yumşaq mobility"
        // TODO: native review
    override val pregnancyConfirmItem1Subtitle: String =
        "Boyun, çiyin və bel üçün kiçik hərəkətlər"
        // TODO: native review
    override val pregnancyConfirmItem2Title: String = "Nəfəs və duruş"
        // TODO: native review
    override val pregnancyConfirmItem2Subtitle: String =
        "Diafraqmal nəfəs · postural ayar"
        // TODO: native review
    override val pregnancyConfirmItem3Title: String = "Pelvic awareness"
        // TODO: native review
    override val pregnancyConfirmItem3Subtitle: String =
        "Yüngül kegel · pelvik döşəmə hissi"
        // TODO: native review
    override val pregnancyConfirmItem4Title: String = "Stretching"
        // TODO: native review
    override val pregnancyConfirmItem4Subtitle: String =
        "Tərpənliş diapazonu üçün təhlükəsiz açma"
        // TODO: native review
    override val pregnancyConfirmMedicalNote: String =
        "Hər hansı məşqə başlamadan əvvəl həkim və ya mütəxəssislə məsləhətləş."
        // TODO: native review
    override val pregnancyConfirmCta: String = "Təhlükəsiz şablonu göstər"
        // TODO: native review
    override val pregnancyConfirmFootnote: String =
        "Sonradan Tənzimləmələrdən dəyişdirə bilərsən"
        // TODO: native review

    // Trimester / Postpartum (Pencil C6Ya4A) — canonical AZ. Safe-template period picker.
    // 3-step mini-flow header ("1 / 3 · DÖVR"); NOT an AI plan trigger.
    override val trimesterEyebrow: String = "1 / 3 · DÖVR"
        // TODO: native review
    override val trimesterTitle: String = "Hansı dövrdəsən?"
        // TODO: native review
    override val trimesterSubtitle: String =
        "Sənə uyğun təhlükəsiz şablonu hazırlamağımıza imkan verir."
        // TODO: native review
    override val trimester1Title: String = "1-ci trimester"
        // TODO: native review
    override val trimester1Subtitle: String = "1–12 həftə · ən yumşaq plan"
        // TODO: native review
    override val trimester2Title: String = "2-ci trimester"
        // TODO: native review
    override val trimester2Subtitle: String = "13–26 həftə · stabil dövr"
        // TODO: native review
    override val trimester3Title: String = "3-cü trimester"
        // TODO: native review
    override val trimester3Subtitle: String = "27–40 həftə · hazırlıq dövrü"
        // TODO: native review
    override val trimesterPostpartumTitle: String = "Postpartum"
        // TODO: native review
    override val trimesterPostpartumSubtitle: String = "Doğumdan sonra (1–12 ay)"
        // TODO: native review
    override val trimesterFootnote: String =
        "Sonradan Tənzimləmələrdən dəyişdirə bilərsən"
        // TODO: native review

    // Safe 4-week Plan (Pencil o0BUd) — canonical AZ. Curated static template,
    // NEVER calls AI plan generation. Default selectedDayIndex=1 (Çərşənbə Axşamı).
    override val safePlanEyebrow: String = "TƏHLÜKƏSİZ ŞABLON"
    override val safePlanTitle: String = "Sənin 4 həftəlik planın"
    override val safePlanSubtitle: String = "2-ci trimester · həftədə 3 gün · 20 dəq"
    override val safePlanMetric1Value: String = "4 həftə"
    override val safePlanMetric1Label: String = "yumşaq"
    override val safePlanMetric2Value: String = "12 sessiya"
    override val safePlanMetric2Label: String = "yumşaq"
    override val safePlanMetric3Value: String = "20 dəq"
    override val safePlanMetric3Label: String = "orta"
    override val safePlanSectionThisWeek: String = "Bu həftə"
    override val safePlanWeekIndicator: String = "Həftə 1 / 4"
    override val safePlanDayMon: String = "B.E"
    override val safePlanDayTue: String = "Ç.A"
    override val safePlanDayWed: String = "Ç"
    override val safePlanDayThu: String = "C.A"
    override val safePlanDayFri: String = "C"
    override val safePlanDaySat: String = "Ş"
    override val safePlanDaySun: String = "B"
    override val safePlanDayDose: String = "15-25 dəq"
    override val safePlanFocusBanner: String =
        "Bu həftənin fokusu: Postural ayar · diafraqmal nəfəs · yumşaq mobility. Çəki məhdudiyyəti: 5 kq."
    override val safePlanCtaToday: String = "Bu günkü məşqi aç"
    override val safePlanCtaInfo: String = "Plan haqqında məlumat"

    override val todaySafeWorkoutTrimesterLabel: String = "2-ci trimester · 18-cü həftə"
    override val todaySafeWorkoutEyebrowToday: String = "BUGÜN"
    override val todaySafeWorkoutEyebrowIntensity: String = "Aşağı intensivlik"
    override val todaySafeWorkoutDateLabel: String = "Şər - 23 May"
    override val todaySafeWorkoutSessionTitle: String = "Yumşaq mobility + nəfəs"
    override val todaySafeWorkoutChipDuration: String = "⏱ 20 dəq"
    override val todaySafeWorkoutChipCount: String = "💪 5 hərəkət"
    override val todaySafeWorkoutChipRpe: String = "🎯 RPE 3-5"
    override val todaySafeWorkoutSectionExercises: String = "Hərəkətlər"
    override val todaySafeWorkoutBadgePregSafe: String = "Pregnancy-safe"
    override val todaySafeWorkoutEx1Title: String = "Diafraqmal nəfəs"
    override val todaySafeWorkoutEx1Stats: String = "3 dəst · 1 dəq"
    override val todaySafeWorkoutEx2Title: String = "Cat-Cow (modifikasiya)"
    override val todaySafeWorkoutEx2Stats: String = "2 dəst · 8 təkrar"
    override val todaySafeWorkoutEx2Badge: String = "yan duruş"
    override val todaySafeWorkoutEx3Title: String = "Pelvic tilt"
    override val todaySafeWorkoutEx3Stats: String = "2 dəst · 10 təkrar"
    override val todaySafeWorkoutEx4Title: String = "Çiyin rulo"
    override val todaySafeWorkoutEx4Stats: String = "2 dəst · 12 təkrar"
    override val todaySafeWorkoutEx5Title: String = "Standing stretch"
    override val todaySafeWorkoutEx5Stats: String = "3 dəst · 30 san"
    override val todaySafeWorkoutCtaStart: String = "Məşqi başla"
    override val todaySafeWorkoutFooter: String =
        "⚠ Hər hansı diskomfortda dayan və həkimlə danış"

    // Exercise Detail (Pregnancy) · Pencil YZ38M — canonical AZ. Curated static template.
    override val exerciseDetailPregSafeBadge: String = "PREGNANCY-SAFE"
        // TODO: native review — badge label remains uppercase token
    override val exerciseDetailPregMediaCaption: String = "GIF · 12 san loop"
        // TODO: native review
    override val exerciseDetailPregAlternative: String = "Alternativ"
        // TODO: native review — swap-link copy in top bar
    override val exerciseDetailPregTitle: String = "Cat-Cow · Yan duruş modifikasiyası"
        // TODO: native review
    override val exerciseDetailPregStat1: String = "Bel + qarın"
        // TODO: native review
    override val exerciseDetailPregStat2: String = "2 dəst · 8 təkrar"
        // TODO: native review
    override val exerciseDetailPregStat3: String = "RPE 3"
        // TODO: native review
    override val exerciseDetailPregModTitle: String = "Trimester modifikasiyası"
        // TODO: native review
    override val exerciseDetailPregModBody: String =
        "2-ci trimestrdə qarın üstə durmaq olmaz. Yan duruşda diz-dirsək poziyasında yumşaq hərəkət edirik."
        // TODO: native review — pregnancy safety guidance, requires medical review
    override val exerciseDetailPregNotesTitle: String = "Diqqət nöqtələri"
        // TODO: native review
    override val exerciseDetailPregNotesCount: String = "3 maddə"
        // TODO: native review
    override val exerciseDetailPregNote1: String = "Nəfəs: cat-da nəfəsi burax, cow-da içəri çək"
        // TODO: native review
    override val exerciseDetailPregNote2: String = "Diskomfort hiss etsən dərhal dayan"
        // TODO: native review
    override val exerciseDetailPregNote3: String = "Bel ağrısı olarsa, addım sayını azalt"
        // TODO: native review
    override val exerciseDetailPregCtaDone: String = "Tamamladım"
        // TODO: native review
    override val exerciseDetailPregSkip: String = "Atla"
        // TODO: native review
    override val exerciseDetailPregEasier: String = "Daha asan variant"
        // TODO: native review

    // Settings · Pregnancy Mode (Pencil vUAuh) — canonical AZ copy.
    override val settingsPregModeTitle: String = "Hamiləlik rejimi"
        // TODO: native review
    override val settingsPregModeStatusPill: String = "AKTIV"
        // TODO: native review
    override val settingsPregModeCardTitle: String = "Hamiləlik rejimi"
        // TODO: native review
    override val settingsPregModeStatPeriodLabel: String = "DÖVR"
        // TODO: native review
    override val settingsPregModeStatPeriodValue: String = "2-ci trimester"
        // TODO: native review
    override val settingsPregModeStatWeekLabel: String = "HƏFTƏ"
        // TODO: native review
    override val settingsPregModeStatWeekValue: String = "18/40"
        // TODO: native review
    override val settingsPregModeStatStartLabel: String = "BAŞLADI"
        // TODO: native review
    override val settingsPregModeStatStartValue: String = "17 yan 2026"
        // TODO: native review
    override val settingsPregModeStatusSub: String =
        "Standart plan generasiyası dayandırılıb · təhlükəsiz şablon aktivdir"
        // TODO: native review — AI hard-stop status line
    override val settingsPregModeSectionManage: String = "İdarə et"
        // TODO: native review
    override val settingsPregModeRowChangePeriodTitle: String = "Dövrü dəyişdir"
        // TODO: native review
    override val settingsPregModeRowChangePeriodSubtitle: String =
        "Trimester və ya həftəni yenilə"
        // TODO: native review
    override val settingsPregModeRowChangePeriodTrailing: String = "2-ci"
        // TODO: native review
    override val settingsPregModeRowPostpartumTitle: String = "Postpartum-a keç"
        // TODO: native review
    override val settingsPregModeRowPostpartumSubtitle: String = "Doğum tarixini bildir"
        // TODO: native review
    override val settingsPregModeRowRemindersTitle: String = "Yumşaq xatırlatma"
        // TODO: native review
    override val settingsPregModeRowRemindersSubtitle: String =
        "Su, nəfəs, qarın istirahəti"
        // TODO: native review
    override val settingsPregModeRowRemindersOn: String = "Açıq"
        // TODO: native review
    override val settingsPregModeRowRemindersOff: String = "Söndürülüb"
        // TODO: native review
    override val settingsPregModeRowDoctorTitle: String = "Həkim qeydləri"
        // TODO: native review
    override val settingsPregModeRowDoctorSubtitle: String = "Vaxt və qeyd əlavə et"
        // TODO: native review
    override val settingsPregModeCtaDisable: String = "Sil"
        // TODO: native review — destructive label
    override val settingsPregModeDisableFooter: String =
        "Rejimi söndürəndə standart plan açılır"
        // TODO: native review

    // V-error variants (iter 32) — AZ canonical ─────────────────────────────
    override val loginErrorPwBadText: String = "Email və ya şifrə yanlışdır"
        // TODO: native review
    override val loginErrorWarning: String =
        "Şifrəni 3 dəfə yanlış girsən, hesabın 5 dəq müvəqqəti bloklanır."
        // TODO: native review

    override val signupExistsEyebrow: String = "QEYDİYYAT" // TODO: native review
    override val signupExistsTitle: String = "EMAİL İLƏ QEYDİYYAT" // TODO: native review
    override val signupExistsEmailError: String = "Bu email artıq qeydiyyatdadır"
        // TODO: native review
    override val signupExistsGoLoginPill: String = "Daxil olə get →" // TODO: native review
    override val signupExistsHelper: String = "Ən az 8 simvol, bir rəqəm." // TODO: native review
    override val signupExistsPrivacyLink: String = "Məxfilik siyasəti" // TODO: native review
    override val signupExistsCta: String = "Qeydiyyatdan keç" // TODO: native review

    override val rateLimitTitle: String = "Çox sayda cəhd" // TODO: native review
    override val rateLimitBody: String =
        "Təhlükəsizlik üçün 15 dəqiqə gözlə. Şifrəni unutdunsa, sıfırlama linki göndərə bilərik."
        // TODO: native review
    override val rateLimitCtaReset: String = "Şifrəni sıfırla" // TODO: native review
    override val rateLimitCtaDismiss: String = "Anladım" // TODO: native review
    override fun rateLimitCountdown(mm: Int, ss: Int): String =
        "⏱ ${mm.toString().padStart(2, '0')}:${ss.toString().padStart(2, '0')} sonra yenidən cəhd et"

    override val offlineBannerText: String = "İnternet bağlantısı yoxdur"
        // TODO: native review
    override val offlineBannerRefresh: String = "🔁 Yenilə" // TODO: native review
    override val offlineFootnote: String = "🔁 Bağlantı qaydanda avtomatik cəhd edilir"
        // TODO: native review

    override val q3SoftWarningText: String =
        "Yaşına görə qoruyucu yanaşma. Sənin yaş aralığında program daha mühafizəkar yük tətbiq edir. " +
            "İlk həftələrdə tədrici progres tövsiyə olunur — davam edə bilərsən."
        // TODO: native review

    override val resetExpiredTitle: String = "Link vaxtı bitib" // TODO: native review
    override val resetExpiredBody: String =
        "Bu sıfırlama linki 15 dəqiqədən çox keçdiyi üçün artıq işləmir. Yenisini istəyə bilərsən."
        // TODO: native review
    override val resetExpiredCtaNew: String = "Yeni link istə" // TODO: native review
    override val resetExpiredCtaBack: String = "Geri qayıt" // TODO: native review
}

// ──────────────────────────────────────────────────────────────────────────────
// RU — placeholder. DO NOT machine-translate; native review required per release.
// ──────────────────────────────────────────────────────────────────────────────
object StringsRu : Strings {
    override val appName: String = "FitLab" // brand mark — identical across locales

    override val splashTagline: String = "Полный фитнес на научной основе"
        // TODO: native review — RU splash tagline

    override val continueAction: String = "Продолжить" // TODO: native review
    override val backAction: String = "Назад" // TODO: native review
    override val skipAction: String = "Позже" // TODO: native review
    override val nextAction: String = "Дальше" // TODO: native review
    override val doneAction: String = "Готово" // TODO: native review

    override val aiDisclosureBody: String =
        "План создаётся AI на научной основе — окончательное решение за тобой, можно изменить в любой момент. " +
            "У премиум-пользователей план перед отправкой проверяется специалистом. " +
            "Это не медицинский совет — при проблемах со здоровьем проконсультируйся с врачом."
            // TODO: native review — RU canonical AI Disclosure, legal sign-off required

    override val medicalHardStopTitle: String = "Беременность или послеродовой период?"
        // TODO: native review
    override val medicalHardStopBody: String =
        "Если ты сообщишь нам, мы подготовим осторожный шаблон. " +
            "В любой момент можно изменить в Настройках."
            // TODO: native review

    override val errorGeneric: String = "Что-то пошло не так" // TODO: native review
    override val errorNetwork: String = "Нет подключения — попробуй снова" // TODO: native review

    override val langSelectTitle: String = "Выбери язык" // TODO: native review — RU lang select title
    override val langSelectNativeNotice: String =
        "Все 3 языка переведены вручную — без машинного перевода"
        // TODO: native review — RU native-translation notice
    override val langOptionAz: String = "Azərbaycanca"
    override val langOptionRu: String = "Русский"
    override val langOptionEn: String = "English"

    // Welcome — RU placeholders, native review required.
    override val welcomeGreeting: String = "ПРИВЕТ" // TODO: native review
    override val welcomeTitleLine1: String = "ПЛАНИРУЙ" // TODO: native review
    override val welcomeTitleLine2: String = "ТРЕНИРОВКУ" // TODO: native review
    override val welcomeSubtitle: String =
        "Супер-сет, дроп-сет, темп — каждый повтор под контролем."
        // TODO: native review
    override val welcomeChipWorkout: String = "тренировку" // TODO: native review
    override val welcomeChipFood: String = "питание" // TODO: native review
    override val welcomeChipEnergy: String = "энергию" // TODO: native review
    override val welcomeChipGoal: String = "цели" // TODO: native review
    // Iter 30 — variant copy (RU placeholders, native review required).
    override val welcomeV1Title: String = "УПРАВЛЯЙ СВОЕЙ ТРЕНИРОВКОЙ" // TODO: native review
    override val welcomeV1Subtitle: String =
        "Планируй, отслеживай, измеряй — каждый шаг под твоим контролем. На науке, с поддержкой AI."
        // TODO: native review
    override val welcomeV2Title: String = "ОТСЛЕЖИВАЙ ПИТАНИЕ" // TODO: native review
    override val welcomeV2Subtitle: String =
        "Азербайджанский стол — дюшбара, долма, плов. Сфотографируй — калории сразу."
        // TODO: native review
    override val welcomeV3Title: String = "ИЗМЕРЯЙ ЭНЕРГИЮ" // TODO: native review
    override val welcomeV3Subtitle: String =
        "Сожжённое, съеденное, выпитое — баланс в реальном времени."
        // TODO: native review
    override val welcomeV4Title: String = "ДОСТИГАЙ ЦЕЛИ" // TODO: native review
    override val welcomeV4Subtitle: String =
        "Streak, milestone, изменение веса. Каждую неделю — ближе на шаг."
        // TODO: native review
    override val welcomePillWorkout: String = "Тренировка" // TODO: native review
    override val welcomePillFood: String = "Питание" // TODO: native review
    override val welcomePillEnergy: String = "Энергия" // TODO: native review
    override val welcomePillGoal: String = "Цель" // TODO: native review
    override val welcomeCtaStart: String = "Начнём" // TODO: native review
    override val welcomeCtaHaveAccount: String = "У меня есть аккаунт" // TODO: native review
    override val languagePillLabel: String = "RU"

    // Q1 · Goal — RU placeholders, native review required.
    override val q1Title: String = "Какая твоя цель?" // TODO: native review
    override val q1Subtitle: String = "План строится по этому выбору" // TODO: native review
    override val q1OptionLoseFatTitle: String = "Снизить вес" // TODO: native review
    override val q1OptionLoseFatSubtitle: String = "Дефицит · сжигание жира" // TODO: native review
    override val q1OptionBuildMuscleTitle: String = "Набрать мышцы" // TODO: native review
    override val q1OptionBuildMuscleSubtitle: String = "Гипертрофия · рост объёма" // TODO: native review
    override val q1OptionGetTonedTitle: String = "Держать форму" // TODO: native review
    override val q1OptionGetTonedSubtitle: String = "Баланс · еженедельная активность" // TODO: native review
    override val q1OptionIncreaseStrengthTitle: String = "Увеличить силу" // TODO: native review
    override val q1OptionIncreaseStrengthSubtitle: String = "Базовые упражнения · рост 1ПМ" // TODO: native review

    // Q2 · Sex — RU placeholders, native review required.
    override val q2Title: String = "Твой пол?" // TODO: native review
    override val q2Subtitle: String = "Расчёт калорий и белка зависит от этого поля" // TODO: native review
    override val q2OptionMaleTitle: String = "Мужской" // TODO: native review
    override val q2OptionMaleSubtitle: String = "Стандартный метаболический базис" // TODO: native review
    override val q2OptionFemaleTitle: String = "Женский" // TODO: native review
    override val q2OptionFemaleSubtitle: String = "Адаптивный расчёт калорий" // TODO: native review
    override val q2OptionPreferNotTitle: String = "Не хочу указывать" // TODO: native review
    override val q2OptionPreferNotSubtitle: String = "Применяется нейтральная таблица" // TODO: native review

    // Q3 · Age — RU placeholders, native review required.
    override val q3Title: String = "Сколько тебе лет?" // TODO: native review
    override val q3Subtitle: String = "Нагрузка рассчитывается по возрасту" // TODO: native review
    override val q3AgeLabel: String = "ВОЗРАСТ" // TODO: native review
    override val q3KeyboardHint: String = "Откроется цифровая клавиатура — нажми и введи" // TODO: native review
    override val q3DialogTitle: String = "Введи возраст" // TODO: native review
    override val q3DialogConfirm: String = "OK" // TODO: native review
    override val q3DialogCancel: String = "Отмена" // TODO: native review

    // Q4 · Height + Weight — RU placeholders, native review required.
    override val q4Title: String = "Твой рост и вес?" // TODO: native review
    override val q4Subtitle: String =
        "Нужно для расчёта калорий, белка и воды"
        // TODO: native review
    override val q4HeightCaption: String = "РОСТ" // TODO: native review
    override val q4WeightCaption: String = "ВЕС" // TODO: native review
    override val q4UnitCm: String = "cm"
    override val q4UnitFt: String = "ft"
    override val q4UnitIn: String = "in"
    override val q4UnitKg: String = "кг" // TODO: native review
    override val q4UnitLb: String = "lb"
    override val q4Hint: String = "Можно обновить в любое время — еженедельно или ежемесячно"
        // TODO: native review
    override val q4DialogHeightTitle: String = "Введи рост" // TODO: native review
    override val q4DialogWeightTitle: String = "Введи вес" // TODO: native review

    // Q5 · Experience — RU placeholders, native review required.
    override val q5Eyebrow: String = "5 / 7 · ОПЫТ" // TODO: native review
    override val q5Title: String = "Каков твой уровень?" // TODO: native review
    override val q5Subtitle: String = "Начнём с подходящей сложности" // TODO: native review
    override val q5OptionBeginnerTitle: String = "Новичок" // TODO: native review
    override val q5OptionBeginnerSubtitle: String = "Первые 6 месяцев или возвращаюсь" // TODO: native review
    override val q5OptionIntermediateTitle: String = "Средний" // TODO: native review
    override val q5OptionIntermediateSubtitle: String = "6 мес – 2 года, тренируюсь регулярно" // TODO: native review
    override val q5OptionAdvancedTitle: String = "Опытный" // TODO: native review
    override val q5OptionAdvancedSubtitle: String = "2+ года, супер-сеты и продвинутые техники" // TODO: native review
    override val q5OptionAthleteTitle: String = "Атлет" // TODO: native review
    override val q5OptionAthleteSubtitle: String = "Готовлюсь к соревнованиям, peak phase" // TODO: native review

    // Q6 · Context — RU placeholders, native review required.
    override val q6Eyebrow: String = "6 / 7 · КОНТЕКСТ" // TODO: native review
    override val q6Title: String = "Где ты тренируешься?" // TODO: native review
    override val q6Subtitle: String = "Это сужает список упражнений и альтернатив"
        // TODO: native review
    override val q6OptionHomeBodyweightTitle: String = "Дома (без оборудования)"
        // TODO: native review
    override val q6OptionHomeBodyweightSubtitle: String = "Только вес тела и поддерживающий режим"
        // TODO: native review
    override val q6OptionHomeEquipmentTitle: String = "Дома (с оборудованием)"
        // TODO: native review
    override val q6OptionHomeEquipmentSubtitle: String = "Гантели, гиря, скамья"
        // TODO: native review
    override val q6OptionGymTitle: String = "В зале" // TODO: native review
    override val q6OptionGymSubtitle: String = "Полное оборудование, силовые тренажёры"
        // TODO: native review
    override val q6OptionHybridTitle: String = "Гибрид (дом + зал)" // TODO: native review
    override val q6OptionHybridSubtitle: String = "Недельный план комбинирует оба"
        // TODO: native review

    // Q7 · Day + Session — RU placeholders, native review required.
    override val q7Eyebrow: String = "7 / 7 · РАСПИСАНИЕ" // TODO: native review
    override val q7Title: String = "Какое у тебя расписание?" // TODO: native review
    override val q7Subtitle: String = "Адаптивный план настроится на эту частоту"
        // TODO: native review
    override val q7DaysCaption: String = "ДНЕЙ В НЕДЕЛЮ" // TODO: native review
    override val q7SessionCaption: String = "ДЛИТЕЛЬНОСТЬ СЕССИИ (МИН)" // TODO: native review

    override fun q7Summary(days: Int, minutes: Int): String =
        "$days тренировок × $minutes мин в неделю"
        // TODO: native review — RU Q7 summary chip

    override val commonFinish: String = "Готово" // TODO: native review

    // Profile Summary — RU placeholders, native review required.
    override val profileSummaryEyebrow: String = "ОБЗОР ПРОФИЛЯ" // TODO: native review
    override val profileSummaryTitle: String = "Всё верно?" // TODO: native review
    override val profileRowGoal: String = "Цель" // TODO: native review
    override val profileRowSex: String = "Пол" // TODO: native review
    override val profileRowAge: String = "Возраст" // TODO: native review
    override val profileRowHeightWeight: String = "Рост · Вес" // TODO: native review
    override val profileRowExperience: String = "Опыт" // TODO: native review
    override val profileRowContext: String = "Контекст" // TODO: native review
    override val profileRowSchedule: String = "Расписание" // TODO: native review
    override val profileSummaryNotice: String =
        "Параметры можно изменить в настройках в любой момент"
        // TODO: native review
    override val profileSummaryCtaCreate: String = "Создать профиль" // TODO: native review
    override val profileSummaryCtaBack: String = "Назад" // TODO: native review

    override fun profileHeightWeightValue(heightCm: Int, weightKg: Double): String {
        val w = if (weightKg % 1.0 == 0.0) weightKg.toInt().toString() else weightKg.toString()
        return "$heightCm см · $w кг"
        // TODO: native review
    }

    // AI Disclosure — RU placeholders, native review required.
    override val aiDisclosureTitle: String = "Твой план создаётся AI"
        // TODO: native review
    override val aiDisclosureSubtitle: String =
        "Каждый план строится на научных правилах + адаптивном AI. Следующее всегда остаётся верным:"
        // TODO: native review
    override val aiDisclosureBullet1: String =
        "Адаптация плана строится на внутренних правилах + AI."
        // TODO: native review
    override val aiDisclosureBullet2: String =
        "Последнее слово за тобой — можешь изменить любое упражнение или день."
        // TODO: native review
    override val aiDisclosureBullet3: String =
        "Твои данные под твоим контролем, удалить можно в любой момент."
        // TODO: native review
    override val aiDisclosureBullet4: String =
        "Проверка соответствия от специалиста откроется в Фазе 2 (premium)."
        // TODO: native review
    override val aiDisclosureCtaConfirm: String = "Понятно, продолжить"
        // TODO: native review
    override val aiDisclosureCtaLearnMore: String = "Подробнее об AI"
        // TODO: native review
    override val aiDisclosureLearnMoreToast: String = "Скоро откроется"
        // TODO: native review

    // Parental Notice (Pencil XG54w) — RU placeholders, native review required.
    override val parentalNoticeEyebrow: String = "ТРЕБУЕТСЯ СОГЛАСИЕ" // TODO: native review
    override val parentalNoticeTitle: String = "Родитель в курсе?" // TODO: native review
    override val parentalNoticeDisclaimer: String =
        "Если тебе меньше 18 лет, для продолжения нужно согласие родителя или опекуна. " +
            "Этого требуют правила health-приложений Apple и Google." // TODO: native review
    override val parentalNoticeConsentLabel: String =
        "Мой родитель/опекун согласен на использование приложения" // TODO: native review
    override val parentalNoticeChipPrivacy: String = "Политика конфиденциальности" // TODO: native review
    override val parentalNoticeChipTerms: String = "Условия использования" // TODO: native review
    override val parentalNoticeCtaContinue: String = "Понятно, продолжить" // TODO: native review
    override val parentalNoticeCtaBack: String = "Назад" // TODO: native review

    // Parental sheet — RU placeholders.
    override val parentalSheetEyebrow: String = "ТРЕБУЕТСЯ СОГЛАСИЕ" // TODO: native review
    override val parentalSheetTitle: String = "Политика конфиденциальности" // TODO: native review
    override val parentalSheetUpdatedLabel: String = "Обновлено: 21 мая 2026" // TODO: native review
    override val parentalSheetLangLabel: String = "RU"
    override val parentalSheetSection1Title: String = "1. Какие данные собираем" // TODO: native review
    override val parentalSheetSection1Body: String =
        "При создании аккаунта собираются email и базовый профиль (возраст, рост, вес, цель). " +
            "Все тренировки и записи о питании хранятся на устройстве и на защищённом сервере." // TODO: native review
    override val parentalSheetSection2Title: String = "2. Как используем" // TODO: native review
    override val parentalSheetSection2Body: String =
        "Данные используются только для подбора плана, отслеживания прогресса и улучшения приложения. " +
            "Третьим лицам не продаём." // TODO: native review
    override val parentalSheetSection3Title: String = "3. Твои права" // TODO: native review
    override val parentalSheetSection3Body: String =
        "В любой момент можно удалить данные (Настройки > Удалить аккаунт). " +
            "Восстановление доступно 30 дней, затем — полное удаление." // TODO: native review
    override val parentalSheetSection4Title: String = "4. Контакты" // TODO: native review
    override val parentalSheetSection4Body: String =
        "Вопросы и жалобы: privacy@fitnessapp.az. Отвечаем в течение 7 дней." // TODO: native review
    override val parentalSheetFooterHint: String = "Прокрути, чтобы прочитать полностью" // TODO: native review
    override val parentalSheetCtaClose: String = "Понятно" // TODO: native review

    // Auth Gate — RU placeholders, native review required.
    override val authGateTitle: String = "Создай аккаунт" // TODO: native review
    override val authGateSubtitle: String =
        "Прогресс безопасно хранится в облаке — доступен и на новом устройстве."
        // TODO: native review
    override val authGateAppleCta: String = "Продолжить с Apple" // TODO: native review
    override val authGateGoogleCta: String = "Продолжить с Google" // TODO: native review
    override val authGateOrDivider: String = "или" // TODO: native review
    override val authGateEmailCta: String = "Продолжить с Email" // TODO: native review
    override val authGateSkipCta: String = "Продолжить без аккаунта (ограниченный режим)"
        // TODO: native review
    override val authGateFooterPrefix: String = "Продолжая, ты принимаешь: "
        // TODO: native review
    override val authGateFooterTerms: String = "Условия" // TODO: native review
    override val authGateFooterPrivacy: String = "Политику конфиденциальности" // TODO: native review
    override val authGateFooterSeparator: String = " · "
    override val legalTodoToast: String = "TODO: правовая страница откроется" // TODO: native review

    // Email Signup — RU placeholders, native review required.
    override val emailSignupTitle: String = "Регистрация по email" // TODO: native review
    override val emailLabelCaption: String = "E-mail" // TODO: native review
    override val emailPlaceholder: String = "name@example.ru" // TODO: native review
    override val passwordLabel: String = "Пароль" // TODO: native review
    override val passwordConfirmLabel: String = "Повтори пароль" // TODO: native review
    override val pwRuleLength: String = "8+ символов" // TODO: native review
    override val pwRuleUppercase: String = "Заглавная" // TODO: native review
    override val pwRuleDigit: String = "Цифра" // TODO: native review
    override val pwRuleSpecial: String = "Спецсимвол" // TODO: native review
    override val emailSignupCta: String = "Зарегистрироваться" // TODO: native review
    override val emailSignupHaveAccount: String = "У меня есть аккаунт. " // TODO: native review
    override val emailSignupLoginLink: String = "Войти" // TODO: native review

    // Email Login — RU placeholders, native review required.
    override val emailLoginTitle: String = "Войти" // TODO: native review
    override val emailLoginForgotPassword: String = "Забыл пароль?" // TODO: native review
    override val emailLoginCta: String = "Войти" // TODO: native review
    override val emailLoginNewUserPrefix: String = "Новый пользователь? " // TODO: native review
    override val emailLoginSignupLink: String = "Регистрация" // TODO: native review

    // Email Verify — RU placeholders, native review required.
    override fun emailVerifySentTo(email: String): String =
        "Код отправлен на $email"
        // TODO: native review

    override val emailVerifySubtitle: String = "Введи 6-значный код подтверждения."
        // TODO: native review

    override fun emailVerifyResendCountdown(secondsLeft: Int): String {
        val mm = (secondsLeft / 60).toString().padStart(2, '0')
        val ss = (secondsLeft % 60).toString().padStart(2, '0')
        return "Отправить код снова ($mm:$ss)"
        // TODO: native review
    }

    override val emailVerifyResendReady: String = "Отправить код снова"
        // TODO: native review
    override val emailVerifyResendToast: String = "Код отправлен" // TODO: native review
    override val emailVerifyWrongEmailPrefix: String = "Неверный e-mail? "
        // TODO: native review
    override val emailVerifyWrongEmailLink: String = "Изменить" // TODO: native review

    // Password Reset · Email — RU placeholders, native review required.
    override val pwdResetEmailTitle: String = "Сбросить пароль" // TODO: native review
    override val pwdResetEmailSubtitle: String =
        "Введи email — отправим ссылку для восстановления"
        // TODO: native review
    override val pwdResetEmailPlaceholder: String = "email@пример.ru"
        // TODO: native review
    override val pwdResetEmailInfo: String = "Ссылка действует 15 минут."
        // TODO: native review
    override val pwdResetEmailCta: String = "Отправить ссылку" // TODO: native review

    // Password Reset · Form — RU placeholders, native review required.
    override val pwdResetFormTitle: String = "Создай новый пароль" // TODO: native review
    override val pwdResetFormNewPasswordLabel: String = "Новый пароль" // TODO: native review
    override val pwdResetFormConfirmLabel: String = "Подтверди пароль" // TODO: native review
    override val pwdResetFormCta: String = "Обновить пароль" // TODO: native review

    // Paywall — RU placeholders, native review required.
    override val paywallEyebrow: String = "ПРЕМИУМ" // TODO: native review
    override val paywallTitle: String = "Готов идти глубже?" // TODO: native review
    override val paywallFeature1: String = "100+ научно обоснованных упражнений (GIF + MP4)" // TODO: native review
    override val paywallFeature2: String = "Безлимитный AI-план + еженедельная адаптация" // TODO: native review
    override val paywallFeature3: String = "Проверка соответствия специалистом" // TODO: native review
    override val paywallFeature4: String = "Безлимитный фото-калории (AZ Top-200)" // TODO: native review
    override val paywallFeature5: String = "Streak Freeze · режим Рамадан" // TODO: native review
    override val paywallTrialTitle: String = "Попробовать 7 дней бесплатно" // TODO: native review
    override val paywallTrialSubtitle: String = "Потом 8 AZN/мес — отмени в любой момент" // TODO: native review
    override val paywallAnnualTitle: String = "Годовой план — 60 AZN" // TODO: native review
    override val paywallAnnualSubtitle: String = "5 AZN/мес · скидка 38%" // TODO: native review
    override val paywallRecommendedBadge: String = "РЕКОМЕНД." // TODO: native review
    override val paywallTransparencyNotice: String =
        "Прозрачная оплата — отмена в один тап, без скрытых платежей."
        // TODO: native review
    override val paywallSmallPrint: String =
        "Правила AI-провайдера · Автопродление будет включено"
        // TODO: native review
    override val paywallCta: String = "Продолжить" // TODO: native review
    override val paywallRestore: String = "Восстановить" // TODO: native review
    override val paywallTerms: String = "Условия" // TODO: native review
    override val paywallPaymentApple: String = "Apple Pay"
    override val paywallPaymentGoogle: String = "Google Pay"
    override val paywallPaymentLocal: String = "m10 / Pulpal"
    override val paywallRestoreToast: String = "TODO: восстановить" // TODO: native review
    override val paywallTermsToast: String = "TODO: условия" // TODO: native review

    // Signout Confirm — RU placeholders, native review required.
    override val signoutTitle: String = "Готов выйти?" // TODO: native review
    override val signoutSubtitle: String =
        "Профиль и офлайн-данные за 30 дней остаются в облаке. При следующем входе всё вернётся автоматически."
        // TODO: native review
    override val signoutStatusBackup: String = "Облачная копия активна" // TODO: native review
    override val signoutStatusOffline: String = "Офлайн-данные ждут 30 дней" // TODO: native review
    override val signoutStatusSync: String = "Автосинхронизация при входе" // TODO: native review
    override val signoutSafetyNotice: String =
        "Даже при удалении с устройства профиль и Premium-статус сохраняются."
        // TODO: native review
    override val signoutCtaConfirm: String = "Выйти" // TODO: native review
    override val signoutCtaCancel: String = "Отмена" // TODO: native review

    // Delete Account · Step 1 — RU placeholders, native review required.
    override val deleteAcc1Title: String = "Готов удалить аккаунт?" // TODO: native review
    override val deleteAcc1Subtitle: String =
        "Указанные ниже данные переходят в soft-архив на 30 дней. " +
            "Если вернётесь — всё восстановится."
        // TODO: native review
    override val deleteAcc1Item1: String = "100+ тренировок и история подходов" // TODO: native review
    override val deleteAcc1Item2: String = "Динамика веса и замеров" // TODO: native review
    override val deleteAcc1Item3: String = "Фото прогресса" // TODO: native review
    override val deleteAcc1Item4: String = "Профиль и настройки" // TODO: native review
    override val deleteAcc1ArchiveChip: String = "архив" // TODO: native review
    override val deleteAcc1RecoveryNotice: String =
        "Если вернётесь в течение 30 дней — все данные восстановятся. " +
            "После 30 дней удаляются полностью."
        // TODO: native review
    override val deleteAcc1CtaCancel: String = "Отмена" // TODO: native review
    override val deleteAcc1CtaContinue: String = "Продолжить" // TODO: native review

    // Delete Account · Step 2 — RU placeholders, native review required.
    override val deleteAcc2Title: String = "Финальное подтверждение" // TODO: native review
    override val deleteAcc2Subtitle: String =
        "Введите SİL в поле ниже, чтобы окончательно удалить аккаунт"
        // TODO: native review — keyword stays AZ
    override val deleteAcc2Caption: String = "Слово подтверждения" // TODO: native review
    override val deleteAcc2Placeholder: String = "SİL" // keyword — do not translate
    override val deleteAcc2Helper: String =
        "Внимание: 'l' нужно ввести заглавной (обязательно)"
        // TODO: native review
    override val deleteAcc2RecoveryNotice: String =
        "Вернётесь в течение 30 дней — данные восстановятся."
        // TODO: native review
    override val deleteAcc2CtaConfirm: String = "Удалить аккаунт" // TODO: native review

    // Pregnancy Nudge — RU placeholders, native review required.
    override val pregnancyNudgeTitle: String = "Беременность или послеродовой период?"
        // TODO: native review
    override val pregnancyNudgeSubtitle: String =
        "Эта информация позволит подготовить безопасный шаблон. " +
            "В любой момент можно изменить в Настройках."
        // TODO: native review
    override val pregnancyNudgeBenefit1: String = "Более щадящая нагрузка" // TODO: native review
    override val pregnancyNudgeBenefit2: String = "Рискованные упражнения исключаются автоматически"
        // TODO: native review
    override val pregnancyNudgeBenefit3: String = "Рекомендуется разрешение врача" // TODO: native review
    override val pregnancyNudgePrivacy: String =
        "Эти данные хранятся в зашифрованном виде и используются только для адаптации плана."
        // TODO: native review
    override val pregnancyNudgeCtaYes: String = "Да, сообщить" // TODO: native review
    override val pregnancyNudgeCtaNo: String = "Не сейчас" // TODO: native review

    // Pregnancy Confirm — RU placeholders, native review required.
    override val pregnancyConfirmTopBarTitle: String = "Безопасный план" // TODO: native review
    override val pregnancyConfirmEyebrow: String = "ОСОБЫЙ ПЕРИОД" // TODO: native review
    override val pregnancyConfirmTitle: String =
        "Твоё здоровье и здоровье малыша важны" // TODO: native review
    override val pregnancyConfirmSubtitle: String =
        "В этот период вместо стандартных тренировок предлагаем безопасные, мягкие движения. " +
            "Совет врача всегда на первом месте."
        // TODO: native review
    override val pregnancyConfirmItem1Title: String = "Мягкая мобильность" // TODO: native review
    override val pregnancyConfirmItem1Subtitle: String =
        "Небольшие движения для шеи, плеч и поясницы" // TODO: native review
    override val pregnancyConfirmItem2Title: String = "Дыхание и осанка" // TODO: native review
    override val pregnancyConfirmItem2Subtitle: String =
        "Диафрагмальное дыхание · постуральная настройка" // TODO: native review
    override val pregnancyConfirmItem3Title: String = "Pelvic awareness" // TODO: native review
    override val pregnancyConfirmItem3Subtitle: String =
        "Лёгкий кегель · ощущение тазового дна" // TODO: native review
    override val pregnancyConfirmItem4Title: String = "Растяжка" // TODO: native review
    override val pregnancyConfirmItem4Subtitle: String =
        "Безопасное раскрытие диапазона движения" // TODO: native review
    override val pregnancyConfirmMedicalNote: String =
        "Перед началом любых упражнений посоветуйся с врачом или специалистом." // TODO: native review
    override val pregnancyConfirmCta: String = "Показать безопасный шаблон" // TODO: native review
    override val pregnancyConfirmFootnote: String =
        "Позже можно изменить в Настройках" // TODO: native review

    // Trimester / Postpartum — RU placeholders, native review required.
    override val trimesterEyebrow: String = "1 / 3 · ПЕРИОД" // TODO: native review
    override val trimesterTitle: String = "В каком ты периоде?" // TODO: native review
    override val trimesterSubtitle: String =
        "Это поможет нам подготовить безопасный шаблон под тебя." // TODO: native review
    override val trimester1Title: String = "1-й триместр" // TODO: native review
    override val trimester1Subtitle: String = "1–12 нед · самый мягкий план" // TODO: native review
    override val trimester2Title: String = "2-й триместр" // TODO: native review
    override val trimester2Subtitle: String = "13–26 нед · стабильный период" // TODO: native review
    override val trimester3Title: String = "3-й триместр" // TODO: native review
    override val trimester3Subtitle: String = "27–40 нед · подготовительный период" // TODO: native review
    override val trimesterPostpartumTitle: String = "Послеродовой период" // TODO: native review
    override val trimesterPostpartumSubtitle: String = "После родов (1–12 мес)" // TODO: native review
    override val trimesterFootnote: String =
        "Позже можно изменить в Настройках" // TODO: native review

    // Safe 4-week Plan — RU placeholders, native review required.
    override val safePlanEyebrow: String = "БЕЗОПАСНЫЙ ШАБЛОН" // TODO: native review
    override val safePlanTitle: String = "Твой план на 4 недели" // TODO: native review
    override val safePlanSubtitle: String = "2-й триместр · 3 дня в неделю · 20 мин" // TODO: native review
    override val safePlanMetric1Value: String = "4 недели" // TODO: native review
    override val safePlanMetric1Label: String = "мягко" // TODO: native review
    override val safePlanMetric2Value: String = "12 сессий" // TODO: native review
    override val safePlanMetric2Label: String = "мягко" // TODO: native review
    override val safePlanMetric3Value: String = "20 мин" // TODO: native review
    override val safePlanMetric3Label: String = "средне" // TODO: native review
    override val safePlanSectionThisWeek: String = "Эта неделя" // TODO: native review
    override val safePlanWeekIndicator: String = "Неделя 1 / 4" // TODO: native review
    override val safePlanDayMon: String = "Пн" // TODO: native review
    override val safePlanDayTue: String = "Вт" // TODO: native review
    override val safePlanDayWed: String = "Ср" // TODO: native review
    override val safePlanDayThu: String = "Чт" // TODO: native review
    override val safePlanDayFri: String = "Пт" // TODO: native review
    override val safePlanDaySat: String = "Сб" // TODO: native review
    override val safePlanDaySun: String = "Вс" // TODO: native review
    override val safePlanDayDose: String = "15-25 мин" // TODO: native review
    override val safePlanFocusBanner: String =
        "Фокус недели: Постуральная настройка · диафрагмальное дыхание · мягкая мобильность. Лимит веса: 5 кг." // TODO: native review
    override val safePlanCtaToday: String = "Открыть тренировку дня" // TODO: native review
    override val safePlanCtaInfo: String = "О плане" // TODO: native review

    override val todaySafeWorkoutTrimesterLabel: String = "2-й триместр · 18-я неделя" // TODO: native review
    override val todaySafeWorkoutEyebrowToday: String = "СЕГОДНЯ" // TODO: native review
    override val todaySafeWorkoutEyebrowIntensity: String = "Низкая интенсивность" // TODO: native review
    override val todaySafeWorkoutDateLabel: String = "Чт - 23 мая" // TODO: native review
    override val todaySafeWorkoutSessionTitle: String = "Мягкая мобильность + дыхание" // TODO: native review
    override val todaySafeWorkoutChipDuration: String = "⏱ 20 мин" // TODO: native review
    override val todaySafeWorkoutChipCount: String = "💪 5 движений" // TODO: native review
    override val todaySafeWorkoutChipRpe: String = "🎯 RPE 3-5" // TODO: native review
    override val todaySafeWorkoutSectionExercises: String = "Упражнения" // TODO: native review
    override val todaySafeWorkoutBadgePregSafe: String = "Безопасно при беременности" // TODO: native review
    override val todaySafeWorkoutEx1Title: String = "Диафрагмальное дыхание" // TODO: native review
    override val todaySafeWorkoutEx1Stats: String = "3 подхода · 1 мин" // TODO: native review
    override val todaySafeWorkoutEx2Title: String = "Cat-Cow (модификация)" // TODO: native review
    override val todaySafeWorkoutEx2Stats: String = "2 подхода · 8 повт." // TODO: native review
    override val todaySafeWorkoutEx2Badge: String = "боковая поза" // TODO: native review
    override val todaySafeWorkoutEx3Title: String = "Pelvic tilt" // TODO: native review
    override val todaySafeWorkoutEx3Stats: String = "2 подхода · 10 повт." // TODO: native review
    override val todaySafeWorkoutEx4Title: String = "Вращение плечами" // TODO: native review
    override val todaySafeWorkoutEx4Stats: String = "2 подхода · 12 повт." // TODO: native review
    override val todaySafeWorkoutEx5Title: String = "Standing stretch" // TODO: native review
    override val todaySafeWorkoutEx5Stats: String = "3 подхода · 30 сек" // TODO: native review
    override val todaySafeWorkoutCtaStart: String = "Начать тренировку" // TODO: native review
    override val todaySafeWorkoutFooter: String =
        "⚠ При любом дискомфорте остановитесь и обратитесь к врачу" // TODO: native review

    override val exerciseDetailPregSafeBadge: String = "PREGNANCY-SAFE" // TODO: native review
    override val exerciseDetailPregMediaCaption: String = "GIF · 12 сек loop" // TODO: native review
    override val exerciseDetailPregAlternative: String = "Альтернатива" // TODO: native review
    override val exerciseDetailPregTitle: String = "Cat-Cow · Боковая модификация" // TODO: native review
    override val exerciseDetailPregStat1: String = "Поясница + живот" // TODO: native review
    override val exerciseDetailPregStat2: String = "2 подхода · 8 повт." // TODO: native review
    override val exerciseDetailPregStat3: String = "RPE 3" // TODO: native review
    override val exerciseDetailPregModTitle: String = "Триместровая модификация" // TODO: native review
    override val exerciseDetailPregModBody: String =
        "Во 2-м триместре нельзя стоять на животе. Делаем мягко в боковой позе на колене и локте." // TODO: native review
    override val exerciseDetailPregNotesTitle: String = "Точки внимания" // TODO: native review
    override val exerciseDetailPregNotesCount: String = "3 пункта" // TODO: native review
    override val exerciseDetailPregNote1: String = "Дыхание: выдох на cat, вдох на cow" // TODO: native review
    override val exerciseDetailPregNote2: String = "При дискомфорте — немедленно остановись" // TODO: native review
    override val exerciseDetailPregNote3: String = "При боли в пояснице — уменьши число повторов" // TODO: native review
    override val exerciseDetailPregCtaDone: String = "Готово" // TODO: native review
    override val exerciseDetailPregSkip: String = "Пропустить" // TODO: native review
    override val exerciseDetailPregEasier: String = "Полегче вариант" // TODO: native review

    override val settingsPregModeTitle: String = "Режим беременности" // TODO: native review
    override val settingsPregModeStatusPill: String = "АКТИВЕН" // TODO: native review
    override val settingsPregModeCardTitle: String = "Режим беременности" // TODO: native review
    override val settingsPregModeStatPeriodLabel: String = "ПЕРИОД" // TODO: native review
    override val settingsPregModeStatPeriodValue: String = "2-й триместр" // TODO: native review
    override val settingsPregModeStatWeekLabel: String = "НЕДЕЛЯ" // TODO: native review
    override val settingsPregModeStatWeekValue: String = "18/40" // TODO: native review
    override val settingsPregModeStatStartLabel: String = "НАЧАТ" // TODO: native review
    override val settingsPregModeStatStartValue: String = "17 янв 2026" // TODO: native review
    override val settingsPregModeStatusSub: String =
        "Генерация стандартного плана приостановлена · активен безопасный шаблон"
        // TODO: native review
    override val settingsPregModeSectionManage: String = "Управление" // TODO: native review
    override val settingsPregModeRowChangePeriodTitle: String = "Изменить период" // TODO: native review
    override val settingsPregModeRowChangePeriodSubtitle: String = "Обнови триместр или неделю" // TODO: native review
    override val settingsPregModeRowChangePeriodTrailing: String = "2-й" // TODO: native review
    override val settingsPregModeRowPostpartumTitle: String = "Перейти к постпартуму" // TODO: native review
    override val settingsPregModeRowPostpartumSubtitle: String = "Сообщи дату родов" // TODO: native review
    override val settingsPregModeRowRemindersTitle: String = "Мягкие напоминания" // TODO: native review
    override val settingsPregModeRowRemindersSubtitle: String = "Вода, дыхание, отдых живота" // TODO: native review
    override val settingsPregModeRowRemindersOn: String = "Вкл" // TODO: native review
    override val settingsPregModeRowRemindersOff: String = "Выкл" // TODO: native review
    override val settingsPregModeRowDoctorTitle: String = "Заметки врача" // TODO: native review
    override val settingsPregModeRowDoctorSubtitle: String = "Добавь дату и заметку" // TODO: native review
    override val settingsPregModeCtaDisable: String = "Удалить" // TODO: native review
    override val settingsPregModeDisableFooter: String =
        "При отключении режима откроется стандартный план"
        // TODO: native review

    // V-error variants (iter 32) — RU placeholder ───────────────────────────
    override val loginErrorPwBadText: String = "Неверный email или пароль" // TODO: native review
    override val loginErrorWarning: String =
        "После 3 неверных попыток аккаунт временно блокируется на 5 мин."
        // TODO: native review

    override val signupExistsEyebrow: String = "РЕГИСТРАЦИЯ" // TODO: native review
    override val signupExistsTitle: String = "РЕГИСТРАЦИЯ ПО EMAIL" // TODO: native review
    override val signupExistsEmailError: String = "Этот email уже зарегистрирован"
        // TODO: native review
    override val signupExistsGoLoginPill: String = "Войти →" // TODO: native review
    override val signupExistsHelper: String = "Не менее 8 символов, одна цифра."
        // TODO: native review
    override val signupExistsPrivacyLink: String = "Политика конфиденциальности"
        // TODO: native review
    override val signupExistsCta: String = "Зарегистрироваться" // TODO: native review

    override val rateLimitTitle: String = "Слишком много попыток" // TODO: native review
    override val rateLimitBody: String =
        "Для безопасности подожди 15 минут. Если забыл пароль — можем отправить ссылку для сброса."
        // TODO: native review
    override val rateLimitCtaReset: String = "Сбросить пароль" // TODO: native review
    override val rateLimitCtaDismiss: String = "Понятно" // TODO: native review
    override fun rateLimitCountdown(mm: Int, ss: Int): String =
        "⏱ Через ${mm.toString().padStart(2, '0')}:${ss.toString().padStart(2, '0')} можно повторить"

    override val offlineBannerText: String = "Нет подключения к интернету" // TODO: native review
    override val offlineBannerRefresh: String = "🔁 Обновить" // TODO: native review
    override val offlineFootnote: String = "🔁 Когда связь вернётся, попробуем автоматически"
        // TODO: native review

    override val q3SoftWarningText: String =
        "Бережный подход по возрасту. В твоём возрастном диапазоне программа применяет более щадящую нагрузку. " +
            "В первые недели рекомендуется постепенный прогресс — можешь продолжить."
        // TODO: native review

    override val resetExpiredTitle: String = "Срок ссылки истёк" // TODO: native review
    override val resetExpiredBody: String =
        "Эта ссылка для сброса больше не работает (прошло больше 15 минут). Можешь запросить новую."
        // TODO: native review
    override val resetExpiredCtaNew: String = "Запросить новую" // TODO: native review
    override val resetExpiredCtaBack: String = "Вернуться" // TODO: native review
}

// ──────────────────────────────────────────────────────────────────────────────
// EN — placeholder. Tertiary locale; native review required per release.
// ──────────────────────────────────────────────────────────────────────────────
object StringsEn : Strings {
    override val appName: String = "FitLab" // brand mark — identical across locales

    override val splashTagline: String = "Science-backed total fitness"
        // TODO: native review — EN splash tagline

    override val continueAction: String = "Continue" // TODO: native review
    override val backAction: String = "Back" // TODO: native review
    override val skipAction: String = "Not now" // TODO: native review
    override val nextAction: String = "Next" // TODO: native review
    override val doneAction: String = "Done" // TODO: native review

    override val aiDisclosureBody: String =
        "Your plan is generated by AI on scientific foundations — the final call is yours, change anytime. " +
            "Premium plans are reviewed by a specialist before delivery. " +
            "This is not medical advice — consult a doctor if you have health concerns."
            // TODO: native review — EN canonical AI Disclosure, legal sign-off required

    override val medicalHardStopTitle: String = "Pregnant or postpartum?"
        // TODO: native review
    override val medicalHardStopBody: String =
        "If you let us know, we'll prepare a cautious template tailored to you. " +
            "You can change this anytime in Settings."
            // TODO: native review

    override val errorGeneric: String = "Something went wrong" // TODO: native review
    override val errorNetwork: String = "No connection — try again" // TODO: native review

    override val langSelectTitle: String = "Choose your language" // TODO: native review — EN lang select title
    override val langSelectNativeNotice: String =
        "All 3 languages hand-translated — no machine translation"
        // TODO: native review — EN native-translation notice
    override val langOptionAz: String = "Azərbaycanca"
    override val langOptionRu: String = "Русский"
    override val langOptionEn: String = "English"

    // Welcome — EN placeholders, native review required.
    override val welcomeGreeting: String = "HELLO" // TODO: native review
    override val welcomeTitleLine1: String = "PLAN YOUR" // TODO: native review
    override val welcomeTitleLine2: String = "WORKOUT" // TODO: native review
    override val welcomeSubtitle: String =
        "Super-set, drop-set, tempo — every rep built with intent."
        // TODO: native review
    override val welcomeChipWorkout: String = "workout" // TODO: native review
    override val welcomeChipFood: String = "nutrition" // TODO: native review
    override val welcomeChipEnergy: String = "energy" // TODO: native review
    override val welcomeChipGoal: String = "goals" // TODO: native review
    // Iter 30 — variant copy (EN placeholders, native review required).
    override val welcomeV1Title: String = "OWN YOUR WORKOUT" // TODO: native review
    override val welcomeV1Subtitle: String =
        "Plan, track, measure — every step under your control. Science-built, AI-assisted."
        // TODO: native review
    override val welcomeV2Title: String = "TRACK YOUR FOOD" // TODO: native review
    override val welcomeV2Subtitle: String =
        "Azerbaijani table — dushbara, dolma, pilaf. Snap a photo, calories ready."
        // TODO: native review
    override val welcomeV3Title: String = "MEASURE YOUR ENERGY" // TODO: native review
    override val welcomeV3Subtitle: String =
        "Burned, eaten, drank — your balance in real time."
        // TODO: native review
    override val welcomeV4Title: String = "REACH YOUR GOAL" // TODO: native review
    override val welcomeV4Subtitle: String =
        "Streaks, milestones, weight change. Every week, one step closer."
        // TODO: native review
    override val welcomePillWorkout: String = "Workout" // TODO: native review
    override val welcomePillFood: String = "Food" // TODO: native review
    override val welcomePillEnergy: String = "Energy" // TODO: native review
    override val welcomePillGoal: String = "Goal" // TODO: native review
    override val welcomeCtaStart: String = "Let's start" // TODO: native review
    override val welcomeCtaHaveAccount: String = "I have an account" // TODO: native review
    override val languagePillLabel: String = "EN"

    // Q1 · Goal — EN placeholders, native review required.
    override val q1Title: String = "What's your goal?" // TODO: native review
    override val q1Subtitle: String = "Your plan is built from this choice" // TODO: native review
    override val q1OptionLoseFatTitle: String = "Lose weight" // TODO: native review
    override val q1OptionLoseFatSubtitle: String = "Deficit · burn fat" // TODO: native review
    override val q1OptionBuildMuscleTitle: String = "Build muscle" // TODO: native review
    override val q1OptionBuildMuscleSubtitle: String = "Hypertrophy · grow volume" // TODO: native review
    override val q1OptionGetTonedTitle: String = "Stay fit" // TODO: native review
    override val q1OptionGetTonedSubtitle: String = "Balance · weekly activity" // TODO: native review
    override val q1OptionIncreaseStrengthTitle: String = "Increase strength" // TODO: native review
    override val q1OptionIncreaseStrengthSubtitle: String = "Compound lifts · 1RM growth" // TODO: native review

    // Q2 · Sex — EN placeholders, native review required.
    override val q2Title: String = "Your sex?" // TODO: native review
    override val q2Subtitle: String = "Calorie and protein math depends on this field" // TODO: native review
    override val q2OptionMaleTitle: String = "Male" // TODO: native review
    override val q2OptionMaleSubtitle: String = "Standard metabolic baseline" // TODO: native review
    override val q2OptionFemaleTitle: String = "Female" // TODO: native review
    override val q2OptionFemaleSubtitle: String = "Adaptive calorie math" // TODO: native review
    override val q2OptionPreferNotTitle: String = "Prefer not to say" // TODO: native review
    override val q2OptionPreferNotSubtitle: String = "Neutral baseline applied" // TODO: native review

    // Q3 · Age — EN placeholders, native review required.
    override val q3Title: String = "How old are you?" // TODO: native review
    override val q3Subtitle: String = "Training load is calibrated to your age" // TODO: native review
    override val q3AgeLabel: String = "AGE" // TODO: native review
    override val q3KeyboardHint: String = "Number keypad opens — tap and type" // TODO: native review
    override val q3DialogTitle: String = "Enter your age" // TODO: native review
    override val q3DialogConfirm: String = "OK" // TODO: native review
    override val q3DialogCancel: String = "Cancel" // TODO: native review

    // Q4 · Height + Weight — EN placeholders, native review required.
    override val q4Title: String = "Your height and weight?" // TODO: native review
    override val q4Subtitle: String =
        "Needed to calculate calorie, protein and water targets"
        // TODO: native review
    override val q4HeightCaption: String = "HEIGHT" // TODO: native review
    override val q4WeightCaption: String = "WEIGHT" // TODO: native review
    override val q4UnitCm: String = "cm"
    override val q4UnitFt: String = "ft"
    override val q4UnitIn: String = "in"
    override val q4UnitKg: String = "kg" // TODO: native review
    override val q4UnitLb: String = "lb"
    override val q4Hint: String = "Update anytime — weekly or monthly"
        // TODO: native review
    override val q4DialogHeightTitle: String = "Enter your height" // TODO: native review
    override val q4DialogWeightTitle: String = "Enter your weight" // TODO: native review

    // Q5 · Experience — EN placeholders, native review required.
    override val q5Eyebrow: String = "5 / 7 · EXPERIENCE" // TODO: native review
    override val q5Title: String = "What's your level?" // TODO: native review
    override val q5Subtitle: String = "Let's start at the right difficulty" // TODO: native review
    override val q5OptionBeginnerTitle: String = "Beginner" // TODO: native review
    override val q5OptionBeginnerSubtitle: String = "First 6 months or returning after a break" // TODO: native review
    override val q5OptionIntermediateTitle: String = "Intermediate" // TODO: native review
    override val q5OptionIntermediateSubtitle: String = "6 mo – 2 yr, training consistently" // TODO: native review
    override val q5OptionAdvancedTitle: String = "Advanced" // TODO: native review
    override val q5OptionAdvancedSubtitle: String = "2+ yr, super-sets and advanced techniques" // TODO: native review
    override val q5OptionAthleteTitle: String = "Athlete" // TODO: native review
    override val q5OptionAthleteSubtitle: String = "Competing, peak phase training" // TODO: native review

    // Q6 · Context — EN placeholders, native review required.
    override val q6Eyebrow: String = "6 / 7 · CONTEXT" // TODO: native review
    override val q6Title: String = "Where do you train?" // TODO: native review
    override val q6Subtitle: String = "This narrows the exercise list and alternatives"
        // TODO: native review
    override val q6OptionHomeBodyweightTitle: String = "Home (no equipment)"
        // TODO: native review
    override val q6OptionHomeBodyweightSubtitle: String = "Bodyweight only, support-mode plan"
        // TODO: native review
    override val q6OptionHomeEquipmentTitle: String = "Home (with equipment)"
        // TODO: native review
    override val q6OptionHomeEquipmentSubtitle: String = "Dumbbell, kettlebell, bench"
        // TODO: native review
    override val q6OptionGymTitle: String = "At the gym" // TODO: native review
    override val q6OptionGymSubtitle: String = "Full equipment, weight machines"
        // TODO: native review
    override val q6OptionHybridTitle: String = "Hybrid (home + gym)" // TODO: native review
    override val q6OptionHybridSubtitle: String = "Weekly plan combines both"
        // TODO: native review

    // Q7 · Day + Session — EN placeholders, native review required.
    override val q7Eyebrow: String = "7 / 7 · SCHEDULE" // TODO: native review
    override val q7Title: String = "How should your schedule look?" // TODO: native review
    override val q7Subtitle: String = "Your adaptive plan will train at this cadence"
        // TODO: native review
    override val q7DaysCaption: String = "DAYS PER WEEK" // TODO: native review
    override val q7SessionCaption: String = "SESSION DURATION (MIN)" // TODO: native review

    override fun q7Summary(days: Int, minutes: Int): String =
        "$days workouts × $minutes min per week"
        // TODO: native review — EN Q7 summary chip

    override val commonFinish: String = "Finish" // TODO: native review

    // Profile Summary — EN placeholders, native review required.
    override val profileSummaryEyebrow: String = "PROFILE SUMMARY" // TODO: native review
    override val profileSummaryTitle: String = "Does everything look right?" // TODO: native review
    override val profileRowGoal: String = "Goal" // TODO: native review
    override val profileRowSex: String = "Sex" // TODO: native review
    override val profileRowAge: String = "Age" // TODO: native review
    override val profileRowHeightWeight: String = "Height · Weight" // TODO: native review
    override val profileRowExperience: String = "Experience" // TODO: native review
    override val profileRowContext: String = "Context" // TODO: native review
    override val profileRowSchedule: String = "Schedule" // TODO: native review
    override val profileSummaryNotice: String =
        "You can update these settings anytime"
        // TODO: native review
    override val profileSummaryCtaCreate: String = "Create my profile" // TODO: native review
    override val profileSummaryCtaBack: String = "Go back" // TODO: native review

    override fun profileHeightWeightValue(heightCm: Int, weightKg: Double): String {
        val w = if (weightKg % 1.0 == 0.0) weightKg.toInt().toString() else weightKg.toString()
        return "$heightCm cm · $w kg"
        // TODO: native review
    }

    // AI Disclosure — EN placeholders, native review required.
    override val aiDisclosureTitle: String = "Your plan is built with AI"
        // TODO: native review
    override val aiDisclosureSubtitle: String =
        "Every plan is shaped by science-backed rules + adaptive AI. These always hold true:"
        // TODO: native review
    override val aiDisclosureBullet1: String =
        "Plan adaptation runs on internal rules + AI."
        // TODO: native review
    override val aiDisclosureBullet2: String =
        "The final call is yours — swap any exercise or day."
        // TODO: native review
    override val aiDisclosureBullet3: String =
        "Your data is yours; delete it anytime."
        // TODO: native review
    override val aiDisclosureBullet4: String =
        "Specialist fit review unlocks in Phase 2 (premium)."
        // TODO: native review
    override val aiDisclosureCtaConfirm: String = "Got it, continue"
        // TODO: native review
    override val aiDisclosureCtaLearnMore: String = "Learn more about AI"
        // TODO: native review
    override val aiDisclosureLearnMoreToast: String = "Coming soon"
        // TODO: native review

    // Parental Notice (Pencil XG54w) — EN placeholders, native review required.
    override val parentalNoticeEyebrow: String = "CONSENT REQUIRED" // TODO: native review
    override val parentalNoticeTitle: String = "Does a parent know?" // TODO: native review
    override val parentalNoticeDisclaimer: String =
        "If you are under 18, you need a parent or guardian to consent before continuing. " +
            "Apple and Google health app policies require this." // TODO: native review
    override val parentalNoticeConsentLabel: String =
        "My parent/guardian agrees that I use this app" // TODO: native review
    override val parentalNoticeChipPrivacy: String = "Privacy policy" // TODO: native review
    override val parentalNoticeChipTerms: String = "Terms of use" // TODO: native review
    override val parentalNoticeCtaContinue: String = "Got it, continue" // TODO: native review
    override val parentalNoticeCtaBack: String = "Back" // TODO: native review

    // Parental sheet — EN placeholders.
    override val parentalSheetEyebrow: String = "CONSENT REQUIRED" // TODO: native review
    override val parentalSheetTitle: String = "Privacy policy" // TODO: native review
    override val parentalSheetUpdatedLabel: String = "Updated: May 21, 2026" // TODO: native review
    override val parentalSheetLangLabel: String = "EN"
    override val parentalSheetSection1Title: String = "1. What we collect" // TODO: native review
    override val parentalSheetSection1Body: String =
        "When you create an account we collect your email and a basic profile (age, height, weight, goal). " +
            "All workout and food logs are stored on-device and on an encrypted server." // TODO: native review
    override val parentalSheetSection2Title: String = "2. How we use it" // TODO: native review
    override val parentalSheetSection2Body: String =
        "Your data is used only to build a fitting workout plan, track progress, and improve the app. " +
            "We do not sell it to third parties." // TODO: native review
    override val parentalSheetSection3Title: String = "3. Your rights" // TODO: native review
    override val parentalSheetSection3Body: String =
        "You can delete your data at any time (Settings > Delete account). " +
            "Recovery is available for 30 days, after which deletion is permanent." // TODO: native review
    override val parentalSheetSection4Title: String = "4. Contact" // TODO: native review
    override val parentalSheetSection4Body: String =
        "Questions or complaints: privacy@fitnessapp.az. We reply within 7 days." // TODO: native review
    override val parentalSheetFooterHint: String = "Scroll to read the full text" // TODO: native review
    override val parentalSheetCtaClose: String = "Got it" // TODO: native review

    // Auth Gate — EN placeholders, native review required.
    override val authGateTitle: String = "Create your account" // TODO: native review
    override val authGateSubtitle: String =
        "Keep your progress safe in the cloud — pick up on any new device."
        // TODO: native review
    override val authGateAppleCta: String = "Continue with Apple" // TODO: native review
    override val authGateGoogleCta: String = "Continue with Google" // TODO: native review
    override val authGateOrDivider: String = "or" // TODO: native review
    override val authGateEmailCta: String = "Continue with email" // TODO: native review
    override val authGateSkipCta: String = "Continue without account (limited)" // TODO: native review
    override val authGateFooterPrefix: String = "By continuing you accept: " // TODO: native review
    override val authGateFooterTerms: String = "Terms" // TODO: native review
    override val authGateFooterPrivacy: String = "Privacy Policy" // TODO: native review
    override val authGateFooterSeparator: String = " · "
    override val legalTodoToast: String = "TODO: legal page coming soon" // TODO: native review

    // Email Signup — EN placeholders, native review required.
    override val emailSignupTitle: String = "Sign up with email" // TODO: native review
    override val emailLabelCaption: String = "Email" // TODO: native review
    override val emailPlaceholder: String = "name@example.com" // TODO: native review
    override val passwordLabel: String = "Password" // TODO: native review
    override val passwordConfirmLabel: String = "Confirm password" // TODO: native review
    override val pwRuleLength: String = "8+ chars" // TODO: native review
    override val pwRuleUppercase: String = "Uppercase" // TODO: native review
    override val pwRuleDigit: String = "Digit" // TODO: native review
    override val pwRuleSpecial: String = "Special" // TODO: native review
    override val emailSignupCta: String = "Sign up" // TODO: native review
    override val emailSignupHaveAccount: String = "I have an account. " // TODO: native review
    override val emailSignupLoginLink: String = "Log in" // TODO: native review

    // Email Login — EN placeholders, native review required.
    override val emailLoginTitle: String = "Log in" // TODO: native review
    override val emailLoginForgotPassword: String = "Forgot password?" // TODO: native review
    override val emailLoginCta: String = "Log in" // TODO: native review
    override val emailLoginNewUserPrefix: String = "New here? " // TODO: native review
    override val emailLoginSignupLink: String = "Sign up" // TODO: native review

    // Email Verify — EN placeholders, native review required.
    override fun emailVerifySentTo(email: String): String =
        "We sent a code to $email"
        // TODO: native review

    override val emailVerifySubtitle: String = "Enter the 6-digit verification code."
        // TODO: native review

    override fun emailVerifyResendCountdown(secondsLeft: Int): String {
        val mm = (secondsLeft / 60).toString().padStart(2, '0')
        val ss = (secondsLeft % 60).toString().padStart(2, '0')
        return "Resend code ($mm:$ss)"
        // TODO: native review
    }

    override val emailVerifyResendReady: String = "Resend code" // TODO: native review
    override val emailVerifyResendToast: String = "Code sent" // TODO: native review
    override val emailVerifyWrongEmailPrefix: String = "Wrong email? "
        // TODO: native review
    override val emailVerifyWrongEmailLink: String = "Change" // TODO: native review

    // Password Reset · Email — EN placeholders, native review required.
    override val pwdResetEmailTitle: String = "Reset password" // TODO: native review
    override val pwdResetEmailSubtitle: String =
        "Enter your email — we'll send a recovery link"
        // TODO: native review
    override val pwdResetEmailPlaceholder: String = "email@example.com"
        // TODO: native review
    override val pwdResetEmailInfo: String = "Link is valid for 15 minutes."
        // TODO: native review
    override val pwdResetEmailCta: String = "Send link" // TODO: native review

    // Password Reset · Form — EN placeholders, native review required.
    override val pwdResetFormTitle: String = "Set a new password" // TODO: native review
    override val pwdResetFormNewPasswordLabel: String = "New password" // TODO: native review
    override val pwdResetFormConfirmLabel: String = "Confirm password" // TODO: native review
    override val pwdResetFormCta: String = "Update password" // TODO: native review

    // Paywall — EN placeholders, native review required.
    override val paywallEyebrow: String = "PREMIUM"
    override val paywallTitle: String = "Ready to go deeper?" // TODO: native review
    override val paywallFeature1: String = "100+ science-backed exercises (GIF + MP4)" // TODO: native review
    override val paywallFeature2: String = "Unlimited AI plan + weekly adaptation" // TODO: native review
    override val paywallFeature3: String = "Specialist fit review" // TODO: native review
    override val paywallFeature4: String = "Unlimited photo-calorie (AZ Top-200)" // TODO: native review
    override val paywallFeature5: String = "Streak Freeze · Ramadan mode" // TODO: native review
    override val paywallTrialTitle: String = "Try 7 days free" // TODO: native review
    override val paywallTrialSubtitle: String = "Then 8 AZN/mo — cancel anytime" // TODO: native review
    override val paywallAnnualTitle: String = "Annual plan — 60 AZN" // TODO: native review
    override val paywallAnnualSubtitle: String = "5 AZN/mo · 38% off" // TODO: native review
    override val paywallRecommendedBadge: String = "BEST" // TODO: native review
    override val paywallTransparencyNotice: String =
        "Transparent billing — cancel with one tap, no hidden fees."
        // TODO: native review
    override val paywallSmallPrint: String =
        "AI provider terms · Auto-renewal will be enabled"
        // TODO: native review
    override val paywallCta: String = "Continue" // TODO: native review
    override val paywallRestore: String = "Restore" // TODO: native review
    override val paywallTerms: String = "Terms" // TODO: native review
    override val paywallPaymentApple: String = "Apple Pay"
    override val paywallPaymentGoogle: String = "Google Pay"
    override val paywallPaymentLocal: String = "m10 / Pulpal"
    override val paywallRestoreToast: String = "TODO: restore" // TODO: native review
    override val paywallTermsToast: String = "TODO: terms" // TODO: native review

    // Signout Confirm — EN placeholders, native review required.
    override val signoutTitle: String = "Ready to sign out?" // TODO: native review
    override val signoutSubtitle: String =
        "Your profile and 30 days of offline data stay in the cloud. We'll restore everything on next sign-in."
        // TODO: native review
    override val signoutStatusBackup: String = "Cloud backup active" // TODO: native review
    override val signoutStatusOffline: String = "Offline data waits 30 days" // TODO: native review
    override val signoutStatusSync: String = "Auto-sync on next sign-in" // TODO: native review
    override val signoutSafetyNotice: String =
        "Even if uninstalled, your profile and Premium status are preserved."
        // TODO: native review
    override val signoutCtaConfirm: String = "Sign out" // TODO: native review
    override val signoutCtaCancel: String = "Cancel" // TODO: native review

    // Delete Account · Step 1 — EN placeholders, native review required.
    override val deleteAcc1Title: String = "Ready to delete your account?" // TODO: native review
    override val deleteAcc1Subtitle: String =
        "The data below moves to a 30-day soft archive. " +
            "Come back within that window and everything is restored."
        // TODO: native review
    override val deleteAcc1Item1: String = "100+ workouts & set history" // TODO: native review
    override val deleteAcc1Item2: String = "Weight & measurement trends" // TODO: native review
    override val deleteAcc1Item3: String = "Progress photos" // TODO: native review
    override val deleteAcc1Item4: String = "Profile & preferences" // TODO: native review
    override val deleteAcc1ArchiveChip: String = "archive" // TODO: native review
    override val deleteAcc1RecoveryNotice: String =
        "Return within 30 days — every byte comes back. " +
            "After 30 days it's permanently deleted."
        // TODO: native review
    override val deleteAcc1CtaCancel: String = "Cancel" // TODO: native review
    override val deleteAcc1CtaContinue: String = "Continue" // TODO: native review

    // Delete Account · Step 2 — EN placeholders, native review required.
    override val deleteAcc2Title: String = "Final confirmation" // TODO: native review
    override val deleteAcc2Subtitle: String =
        "Type SİL in the field below so we can permanently delete your account"
        // TODO: native review — keyword stays AZ
    override val deleteAcc2Caption: String = "Confirmation word" // TODO: native review
    override val deleteAcc2Placeholder: String = "SİL" // keyword — do not translate
    override val deleteAcc2Helper: String =
        "Note: 'l' must be lowercase, 'İ' uppercase (exact match)"
        // TODO: native review
    override val deleteAcc2RecoveryNotice: String =
        "Return within 30 days and your data is restored."
        // TODO: native review
    override val deleteAcc2CtaConfirm: String = "Delete account" // TODO: native review

    // Pregnancy Nudge — EN placeholders, native review required.
    override val pregnancyNudgeTitle: String = "Pregnant or postpartum?"
        // TODO: native review
    override val pregnancyNudgeSubtitle: String =
        "Sharing this helps us prepare a safer template. " +
            "You can change it anytime in Settings."
        // TODO: native review
    override val pregnancyNudgeBenefit1: String = "Gentler load progression" // TODO: native review
    override val pregnancyNudgeBenefit2: String = "Risky movements removed automatically"
        // TODO: native review
    override val pregnancyNudgeBenefit3: String = "Doctor clearance recommended" // TODO: native review
    override val pregnancyNudgePrivacy: String =
        "This data is stored encrypted and used only to adapt your plan."
        // TODO: native review
    override val pregnancyNudgeCtaYes: String = "Yes, let you know" // TODO: native review
    override val pregnancyNudgeCtaNo: String = "Not now" // TODO: native review

    // Pregnancy Confirm — EN placeholders, native review required.
    override val pregnancyConfirmTopBarTitle: String = "Safe plan" // TODO: native review
    override val pregnancyConfirmEyebrow: String = "SPECIAL PERIOD" // TODO: native review
    override val pregnancyConfirmTitle: String =
        "Your and your baby's health matter" // TODO: native review
    override val pregnancyConfirmSubtitle: String =
        "In this period we offer safe, gentle movements instead of a standard workout. " +
            "Your doctor's advice always comes first."
        // TODO: native review
    override val pregnancyConfirmItem1Title: String = "Gentle mobility" // TODO: native review
    override val pregnancyConfirmItem1Subtitle: String =
        "Small movements for neck, shoulders and lower back" // TODO: native review
    override val pregnancyConfirmItem2Title: String = "Breath and posture" // TODO: native review
    override val pregnancyConfirmItem2Subtitle: String =
        "Diaphragmatic breath · postural tune-up" // TODO: native review
    override val pregnancyConfirmItem3Title: String = "Pelvic awareness" // TODO: native review
    override val pregnancyConfirmItem3Subtitle: String =
        "Light kegel · pelvic floor awareness" // TODO: native review
    override val pregnancyConfirmItem4Title: String = "Stretching" // TODO: native review
    override val pregnancyConfirmItem4Subtitle: String =
        "Safe range-of-motion opening" // TODO: native review
    override val pregnancyConfirmMedicalNote: String =
        "Before starting any exercise, consult a doctor or specialist." // TODO: native review
    override val pregnancyConfirmCta: String = "Show safe template" // TODO: native review
    override val pregnancyConfirmFootnote: String =
        "You can change this later from Settings" // TODO: native review

    // Trimester / Postpartum — EN placeholders, native review required.
    override val trimesterEyebrow: String = "1 / 3 · PERIOD" // TODO: native review
    override val trimesterTitle: String = "Which period are you in?" // TODO: native review
    override val trimesterSubtitle: String =
        "Helps us prepare a safe template tailored to you." // TODO: native review
    override val trimester1Title: String = "1st trimester" // TODO: native review
    override val trimester1Subtitle: String = "1–12 weeks · gentlest plan" // TODO: native review
    override val trimester2Title: String = "2nd trimester" // TODO: native review
    override val trimester2Subtitle: String = "13–26 weeks · stable period" // TODO: native review
    override val trimester3Title: String = "3rd trimester" // TODO: native review
    override val trimester3Subtitle: String = "27–40 weeks · preparation period" // TODO: native review
    override val trimesterPostpartumTitle: String = "Postpartum" // TODO: native review
    override val trimesterPostpartumSubtitle: String = "After birth (1–12 months)" // TODO: native review
    override val trimesterFootnote: String =
        "You can change this later from Settings" // TODO: native review

    // Safe 4-week Plan — EN placeholders, native review required.
    override val safePlanEyebrow: String = "SAFE TEMPLATE" // TODO: native review
    override val safePlanTitle: String = "Your 4-week plan" // TODO: native review
    override val safePlanSubtitle: String = "2nd trimester · 3 days/week · 20 min" // TODO: native review
    override val safePlanMetric1Value: String = "4 weeks" // TODO: native review
    override val safePlanMetric1Label: String = "gentle" // TODO: native review
    override val safePlanMetric2Value: String = "12 sessions" // TODO: native review
    override val safePlanMetric2Label: String = "gentle" // TODO: native review
    override val safePlanMetric3Value: String = "20 min" // TODO: native review
    override val safePlanMetric3Label: String = "moderate" // TODO: native review
    override val safePlanSectionThisWeek: String = "This week" // TODO: native review
    override val safePlanWeekIndicator: String = "Week 1 / 4" // TODO: native review
    override val safePlanDayMon: String = "Mon" // TODO: native review
    override val safePlanDayTue: String = "Tue" // TODO: native review
    override val safePlanDayWed: String = "Wed" // TODO: native review
    override val safePlanDayThu: String = "Thu" // TODO: native review
    override val safePlanDayFri: String = "Fri" // TODO: native review
    override val safePlanDaySat: String = "Sat" // TODO: native review
    override val safePlanDaySun: String = "Sun" // TODO: native review
    override val safePlanDayDose: String = "15-25 min" // TODO: native review
    override val safePlanFocusBanner: String =
        "This week's focus: Postural alignment · diaphragmatic breathing · gentle mobility. Weight limit: 5 kg." // TODO: native review
    override val safePlanCtaToday: String = "Open today's workout" // TODO: native review
    override val safePlanCtaInfo: String = "About this plan" // TODO: native review

    override val todaySafeWorkoutTrimesterLabel: String = "2nd trimester · week 18" // TODO: native review
    override val todaySafeWorkoutEyebrowToday: String = "TODAY" // TODO: native review
    override val todaySafeWorkoutEyebrowIntensity: String = "Low intensity" // TODO: native review
    override val todaySafeWorkoutDateLabel: String = "Thu - May 23" // TODO: native review
    override val todaySafeWorkoutSessionTitle: String = "Gentle mobility + breath" // TODO: native review
    override val todaySafeWorkoutChipDuration: String = "⏱ 20 min" // TODO: native review
    override val todaySafeWorkoutChipCount: String = "💪 5 exercises" // TODO: native review
    override val todaySafeWorkoutChipRpe: String = "🎯 RPE 3-5" // TODO: native review
    override val todaySafeWorkoutSectionExercises: String = "Exercises" // TODO: native review
    override val todaySafeWorkoutBadgePregSafe: String = "Pregnancy-safe" // TODO: native review
    override val todaySafeWorkoutEx1Title: String = "Diaphragmatic breathing" // TODO: native review
    override val todaySafeWorkoutEx1Stats: String = "3 sets · 1 min" // TODO: native review
    override val todaySafeWorkoutEx2Title: String = "Cat-Cow (modified)" // TODO: native review
    override val todaySafeWorkoutEx2Stats: String = "2 sets · 8 reps" // TODO: native review
    override val todaySafeWorkoutEx2Badge: String = "side-lying" // TODO: native review
    override val todaySafeWorkoutEx3Title: String = "Pelvic tilt" // TODO: native review
    override val todaySafeWorkoutEx3Stats: String = "2 sets · 10 reps" // TODO: native review
    override val todaySafeWorkoutEx4Title: String = "Shoulder rolls" // TODO: native review
    override val todaySafeWorkoutEx4Stats: String = "2 sets · 12 reps" // TODO: native review
    override val todaySafeWorkoutEx5Title: String = "Standing stretch" // TODO: native review
    override val todaySafeWorkoutEx5Stats: String = "3 sets · 30 sec" // TODO: native review
    override val todaySafeWorkoutCtaStart: String = "Start workout" // TODO: native review
    override val todaySafeWorkoutFooter: String =
        "⚠ Stop on any discomfort and consult your doctor" // TODO: native review

    override val exerciseDetailPregSafeBadge: String = "PREGNANCY-SAFE" // TODO: native review
    override val exerciseDetailPregMediaCaption: String = "GIF · 12 sec loop" // TODO: native review
    override val exerciseDetailPregAlternative: String = "Alternative" // TODO: native review
    override val exerciseDetailPregTitle: String = "Cat-Cow · Side-lying modification" // TODO: native review
    override val exerciseDetailPregStat1: String = "Lower back + core" // TODO: native review
    override val exerciseDetailPregStat2: String = "2 sets · 8 reps" // TODO: native review
    override val exerciseDetailPregStat3: String = "RPE 3" // TODO: native review
    override val exerciseDetailPregModTitle: String = "Trimester modification" // TODO: native review
    override val exerciseDetailPregModBody: String =
        "In 2nd trimester avoid prone position. Use a side-lying hands-and-knees posture and move gently." // TODO: native review
    override val exerciseDetailPregNotesTitle: String = "Focus points" // TODO: native review
    override val exerciseDetailPregNotesCount: String = "3 items" // TODO: native review
    override val exerciseDetailPregNote1: String = "Breath: exhale on cat, inhale on cow" // TODO: native review
    override val exerciseDetailPregNote2: String = "Stop immediately if you feel discomfort" // TODO: native review
    override val exerciseDetailPregNote3: String = "If lower back pain — reduce the rep count" // TODO: native review
    override val exerciseDetailPregCtaDone: String = "Done" // TODO: native review
    override val exerciseDetailPregSkip: String = "Skip" // TODO: native review
    override val exerciseDetailPregEasier: String = "Easier variant" // TODO: native review

    override val settingsPregModeTitle: String = "Pregnancy mode" // TODO: native review
    override val settingsPregModeStatusPill: String = "ACTIVE" // TODO: native review
    override val settingsPregModeCardTitle: String = "Pregnancy mode" // TODO: native review
    override val settingsPregModeStatPeriodLabel: String = "PERIOD" // TODO: native review
    override val settingsPregModeStatPeriodValue: String = "2nd trimester" // TODO: native review
    override val settingsPregModeStatWeekLabel: String = "WEEK" // TODO: native review
    override val settingsPregModeStatWeekValue: String = "18/40" // TODO: native review
    override val settingsPregModeStatStartLabel: String = "STARTED" // TODO: native review
    override val settingsPregModeStatStartValue: String = "Jan 17 2026" // TODO: native review
    override val settingsPregModeStatusSub: String =
        "Standard plan generation paused · safe template active"
        // TODO: native review
    override val settingsPregModeSectionManage: String = "Manage" // TODO: native review
    override val settingsPregModeRowChangePeriodTitle: String = "Change period" // TODO: native review
    override val settingsPregModeRowChangePeriodSubtitle: String = "Update trimester or week" // TODO: native review
    override val settingsPregModeRowChangePeriodTrailing: String = "2nd" // TODO: native review
    override val settingsPregModeRowPostpartumTitle: String = "Switch to postpartum" // TODO: native review
    override val settingsPregModeRowPostpartumSubtitle: String = "Report birth date" // TODO: native review
    override val settingsPregModeRowRemindersTitle: String = "Gentle reminders" // TODO: native review
    override val settingsPregModeRowRemindersSubtitle: String = "Water, breath, belly rest" // TODO: native review
    override val settingsPregModeRowRemindersOn: String = "On" // TODO: native review
    override val settingsPregModeRowRemindersOff: String = "Off" // TODO: native review
    override val settingsPregModeRowDoctorTitle: String = "Doctor notes" // TODO: native review
    override val settingsPregModeRowDoctorSubtitle: String = "Add a date and note" // TODO: native review
    override val settingsPregModeCtaDisable: String = "Delete" // TODO: native review
    override val settingsPregModeDisableFooter: String =
        "Disabling the mode reopens the standard plan"
        // TODO: native review

    // V-error variants (iter 32) — EN placeholder ───────────────────────────
    override val loginErrorPwBadText: String = "Wrong email or password" // TODO: native review
    override val loginErrorWarning: String =
        "After 3 wrong attempts your account is locked for 5 min."
        // TODO: native review

    override val signupExistsEyebrow: String = "SIGN UP" // TODO: native review
    override val signupExistsTitle: String = "SIGN UP WITH EMAIL" // TODO: native review
    override val signupExistsEmailError: String = "This email is already registered"
        // TODO: native review
    override val signupExistsGoLoginPill: String = "Go to login →" // TODO: native review
    override val signupExistsHelper: String = "At least 8 chars, one digit." // TODO: native review
    override val signupExistsPrivacyLink: String = "Privacy policy" // TODO: native review
    override val signupExistsCta: String = "Sign up" // TODO: native review

    override val rateLimitTitle: String = "Too many attempts" // TODO: native review
    override val rateLimitBody: String =
        "For your safety wait 15 minutes. If you forgot your password we can send you a reset link."
        // TODO: native review
    override val rateLimitCtaReset: String = "Reset password" // TODO: native review
    override val rateLimitCtaDismiss: String = "Got it" // TODO: native review
    override fun rateLimitCountdown(mm: Int, ss: Int): String =
        "⏱ Retry in ${mm.toString().padStart(2, '0')}:${ss.toString().padStart(2, '0')}"

    override val offlineBannerText: String = "No internet connection" // TODO: native review
    override val offlineBannerRefresh: String = "🔁 Retry" // TODO: native review
    override val offlineFootnote: String = "🔁 We will retry automatically when the connection is back"
        // TODO: native review

    override val q3SoftWarningText: String =
        "Protective approach for your age. In your age range the program uses a more conservative load. " +
            "Gradual progression is recommended for the first weeks — you can continue."
        // TODO: native review

    override val resetExpiredTitle: String = "Link expired" // TODO: native review
    override val resetExpiredBody: String =
        "This reset link no longer works because more than 15 minutes have passed. You can request a new one."
        // TODO: native review
    override val resetExpiredCtaNew: String = "Request a new link" // TODO: native review
    override val resetExpiredCtaBack: String = "Go back" // TODO: native review
}

/**
 * Resolve the appropriate Strings table for a 2-letter language tag.
 * Falls back to AZ (primary locale).
 */
fun resolveStrings(languageTag: String): Strings = when (languageTag.lowercase().take(2)) {
    "az" -> StringsAz
    "ru" -> StringsRu
    "en" -> StringsEn
    else -> StringsAz
}

/** Default to AZ until a platform locale is provided. */
val LocalStrings = compositionLocalOf<Strings> { StringsAz }

/** Platform-resolved current locale (2-letter language tag). */
expect fun currentLocale(): String

@Composable
fun StringsProvider(content: @Composable () -> Unit) {
    val strings = remember { resolveStrings(currentLocale()) }
    CompositionLocalProvider(LocalStrings provides strings, content = content)
}
