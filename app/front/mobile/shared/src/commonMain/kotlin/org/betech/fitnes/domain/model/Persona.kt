package org.betech.fitnes.domain.model

import kotlinx.serialization.Serializable

/**
 * Persona cell — `{context × sex × goal}` per `docs/project-context.md §6`.
 * `cellId` matches Postgres `user_profiles.persona_cell` (`"serious_gym.male.bulk"`).
 */
@Serializable
data class Persona(
    val context: TrainingContext,
    val sex: Sex,
    val goal: GoalType
) {
    val cellId: String
        get() = "${context.wire()}.${sex.wire()}.${goal.wire()}"
}

private fun TrainingContext.wire(): String = when (this) {
    TrainingContext.SERIOUS_GYM -> "serious_gym"
    TrainingContext.CASUAL_GYM -> "casual_gym"
    TrainingContext.HOME_ONLY -> "home_only"
}

private fun Sex.wire(): String = when (this) {
    Sex.MALE -> "male"
    Sex.FEMALE -> "female"
}

private fun GoalType.wire(): String = when (this) {
    GoalType.BULK -> "bulk"
    GoalType.CUT -> "cut"
    GoalType.GENERAL_FIT -> "general_fit"
}

/** Deterministic persona resolver — no AI, no randomness. */
object PersonaResolver {
    fun resolve(context: TrainingContext, sex: Sex, goal: GoalType): Persona =
        Persona(context, sex, goal)
}
