package org.betech.fitnes.domain.usecase

import org.betech.fitnes.domain.model.GoalType
import org.betech.fitnes.domain.model.Persona
import org.betech.fitnes.domain.model.PersonaResolver
import org.betech.fitnes.domain.model.Sex
import org.betech.fitnes.domain.model.TrainingContext

class ResolvePersonaUseCase {
    operator fun invoke(context: TrainingContext, sex: Sex, goal: GoalType): Persona =
        PersonaResolver.resolve(context, sex, goal)
}
