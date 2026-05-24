package org.betech.fitnes.data.source.remote.mock

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.betech.fitnes.data.source.remote.RemoteSource
import kotlin.random.Random

/**
 * In-memory fake of [RemoteSource] — no real network.
 * Holds state in [MutableStateFlow] so observe* methods react to writes.
 * Latency simulated via [delay] (300..800 ms) per call.
 */
class MockRemoteSource(
    @Suppress("unused") private val json: Json
) : RemoteSource {

    private val profileFlow = MutableStateFlow<String?>(INITIAL_PROFILE_JSON)
    private val progressFlow = MutableStateFlow(INITIAL_PROGRESS_JSON)
    private val sessionFlow = MutableStateFlow<String?>(null)

    private suspend fun fakeLatency() {
        delay(Random.nextLong(300L, 800L))
    }

    // ── Profile ──
    override suspend fun fetchProfileJson(): String? {
        fakeLatency()
        return profileFlow.value
    }

    override suspend fun saveProfileJson(json: String) {
        fakeLatency()
        profileFlow.value = json
    }

    override fun observeProfileJson(): Flow<String?> = profileFlow.asStateFlow()

    // ── Onboarding ──
    override suspend fun fetchOnboardingProgressJson(): String {
        fakeLatency()
        return progressFlow.value
    }

    override suspend fun saveOnboardingAnswer(stepId: String, answerJson: String) {
        fakeLatency()
        // Mock: we don't merge JSON here — repository owns the merge & re-encodes.
        // This method exists so the wire protocol is symmetric with future Supabase impl.
    }

    override suspend fun advanceOnboardingTo(stepJson: String) {
        fakeLatency()
        // No-op in mock; repository updates the flow via saveProgressJson sequence.
    }

    override suspend fun resetOnboarding() {
        fakeLatency()
        progressFlow.value = INITIAL_PROGRESS_JSON
    }

    override fun observeOnboardingProgressJson(): Flow<String> = progressFlow.asStateFlow()

    /** Internal hook used by repository to persist full progress snapshot. */
    fun pushProgressJson(json: String) {
        progressFlow.value = json
    }

    // ── Questions ──
    override suspend fun fetchOnboardingQuestionsJson(): String {
        fakeLatency()
        return ONBOARDING_QUESTIONS_JSON
    }

    // ── Analytics ──
    override suspend fun emitAnalytics(eventJson: String) {
        fakeLatency()
        println("[MockRemoteSource] analytics: $eventJson")
    }

    // ── Auth ──
    override suspend fun signIn(email: String, password: String): String {
        fakeLatency()
        val session = mockSessionJson(email, method = "email")
        sessionFlow.value = session
        return session
    }

    override suspend fun signUp(email: String, password: String): String {
        fakeLatency()
        val session = mockSessionJson(email, method = "email")
        sessionFlow.value = session
        return session
    }

    override suspend fun signInApple(idToken: String): String {
        fakeLatency()
        val session = mockSessionJson("apple-user@privaterelay.appleid.com", method = "apple")
        sessionFlow.value = session
        return session
    }

    override suspend fun signInGoogle(idToken: String): String {
        fakeLatency()
        val session = mockSessionJson("google-user@gmail.com", method = "google")
        sessionFlow.value = session
        return session
    }

    override suspend fun signOut() {
        fakeLatency()
        sessionFlow.value = null
    }

    override fun observeSessionJson(): Flow<String?> = sessionFlow.asStateFlow()

    private fun mockSessionJson(email: String, method: String): String = """
        {
            "userId": "mock-user-001",
            "email": "$email",
            "method": "$method",
            "issuedAt": "2026-05-24T10:00:00Z",
            "accessTokenPreview": "mock-token-xyz"
        }
    """.trimIndent()

    private companion object {
        // 27-field UserProfile (7 required + 20 optional populated where useful).
        // TODO: native review — strings here are placeholders, not user-facing AZ copy.
        private const val INITIAL_PROFILE_JSON = """
        {
            "user_id": "mock-user-001",
            "email": "demo@fitnes.az",
            "goal": "bulk",
            "gender": "male",
            "age": 28,
            "height_cm": 175,
            "weight_kg": 75.0,
            "experience_level": "intermediate",
            "context": "casual_gym",
            "weekly_days": 4,
            "session_duration_min": 45,
            "persona_cell": "casual_gym.male.bulk",
            "ai_disclosure_accepted_at": "2026-05-24T09:00:00Z",
            "parental_consent_at": null,
            "pregnancy_postpartum": "none",
            "language_at_signup": "az",
            "activity_level_daily": "moderate",
            "target_weight_kg": 80.0,
            "target_deadline": "2026-12-31",
            "body_fat_visual_estimate": 3,
            "frame_size": "medium",
            "sleep_h_per_night": 7.0,
            "stress_pss4_score": 6,
            "sedentary_hours_per_day": 6.5,
            "step_goal": 8000,
            "injury_history": [],
            "movement_restrictions": [],
            "diet_preference": "omnivore",
            "allergies": [],
            "food_intolerances": [],
            "religious_dietary": null,
            "ramazan_active": false,
            "equipment_inventory": ["dumbbells", "bench", "pull_up_bar"],
            "motivations": ["look_better", "feel_stronger"],
            "notification_cadence": "daily",
            "trainer_voice_preference": "neutral",
            "created_at": "2026-05-20T08:00:00Z",
            "updated_at": "2026-05-24T09:00:00Z"
        }
        """

        // Initial onboarding: at Welcome with no answers (cold start).
        private const val INITIAL_PROGRESS_JSON = """
        {
            "currentStep": { "type": "welcome" },
            "completedSteps": [],
            "answers": {},
            "startedAt": null,
            "updatedAt": null
        }
        """

        // Question registry — covers every L1 step + key gates.
        // TODO: native review for all label/sub keys before launch.
        private const val ONBOARDING_QUESTIONS_JSON = """
        [
            {
                "id": "q1_goal",
                "step": { "type": "goal_selection" },
                "kind": "single_choice",
                "layer": "L1",
                "titleKey": "onb.q1.title",
                "subContextKey": "onb.q1.sub",
                "skippable": false,
                "options": [
                    { "value": "bulk", "labelKey": "goal.bulk", "iconKey": "ic_bulk" },
                    { "value": "cut", "labelKey": "goal.cut", "iconKey": "ic_cut" },
                    { "value": "general_fit", "labelKey": "goal.general_fit", "iconKey": "ic_fit" }
                ],
                "validation": null,
                "analyticsId": "goal_answered"
            },
            {
                "id": "q2_gender",
                "step": { "type": "sex" },
                "kind": "single_choice",
                "layer": "L1",
                "titleKey": "onb.q2.title",
                "subContextKey": "onb.q2.sub",
                "skippable": false,
                "options": [
                    { "value": "male", "labelKey": "sex.male" },
                    { "value": "female", "labelKey": "sex.female" }
                ],
                "validation": null,
                "analyticsId": "sex_answered"
            },
            {
                "id": "q3_age",
                "step": { "type": "age" },
                "kind": "wheel_numeric",
                "layer": "L1",
                "titleKey": "onb.q3.title",
                "subContextKey": "onb.q3.sub",
                "skippable": false,
                "options": [],
                "validation": { "minInt": 13, "maxInt": 99, "errorCode": "ONB_001" },
                "analyticsId": "age_answered"
            },
            {
                "id": "q4_height_weight",
                "step": { "type": "height_weight" },
                "kind": "height_weight",
                "layer": "L1",
                "titleKey": "onb.q4.title",
                "subContextKey": "onb.q4.sub",
                "skippable": false,
                "options": [],
                "validation": { "minInt": 100, "maxInt": 230, "errorCode": "ONB_002" },
                "analyticsId": "height_weight_answered"
            },
            {
                "id": "q5_experience",
                "step": { "type": "experience" },
                "kind": "single_choice",
                "layer": "L1",
                "titleKey": "onb.q5.title",
                "subContextKey": "onb.q5.sub",
                "skippable": false,
                "options": [
                    { "value": "beginner", "labelKey": "exp.beginner" },
                    { "value": "intermediate", "labelKey": "exp.intermediate" },
                    { "value": "advanced", "labelKey": "exp.advanced" }
                ],
                "validation": null,
                "analyticsId": "experience_answered"
            },
            {
                "id": "q6_context",
                "step": { "type": "context" },
                "kind": "single_choice",
                "layer": "L1",
                "titleKey": "onb.q6.title",
                "subContextKey": "onb.q6.sub",
                "skippable": false,
                "options": [
                    { "value": "serious_gym", "labelKey": "ctx.serious_gym" },
                    { "value": "casual_gym", "labelKey": "ctx.casual_gym" },
                    { "value": "home_only", "labelKey": "ctx.home_only" }
                ],
                "validation": null,
                "analyticsId": "context_answered"
            },
            {
                "id": "q7_days_session",
                "step": { "type": "days_session" },
                "kind": "days_session",
                "layer": "L1",
                "titleKey": "onb.q7.title",
                "subContextKey": "onb.q7.sub",
                "skippable": false,
                "options": [],
                "validation": { "minInt": 2, "maxInt": 7 },
                "analyticsId": "days_session_answered"
            },
            {
                "id": "q_pregnancy",
                "step": { "type": "pregnancy_nudge" },
                "kind": "single_choice",
                "layer": "L5",
                "titleKey": "onb.preg.title",
                "subContextKey": "onb.preg.sub",
                "skippable": true,
                "options": [
                    { "value": "none", "labelKey": "preg.none" },
                    { "value": "pregnant", "labelKey": "preg.pregnant" },
                    { "value": "postpartum_lt_6m", "labelKey": "preg.postpartum_lt_6m" },
                    { "value": "postpartum_gt_6m", "labelKey": "preg.postpartum_gt_6m" }
                ],
                "validation": null,
                "analyticsId": "pregnancy_nudge_response"
            }
        ]
        """
    }
}
