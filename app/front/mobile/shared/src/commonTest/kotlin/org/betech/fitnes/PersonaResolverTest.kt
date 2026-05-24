package org.betech.fitnes

import org.betech.fitnes.domain.model.GoalType
import org.betech.fitnes.domain.model.PersonaResolver
import org.betech.fitnes.domain.model.Sex
import org.betech.fitnes.domain.model.TrainingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class PersonaResolverTest {

    @Test
    fun `resolves all context x sex x goal cells deterministically`() {
        val contexts = TrainingContext.entries
        val sexes = Sex.entries
        val goals = GoalType.entries
        var count = 0
        for (c in contexts) for (s in sexes) for (g in goals) {
            val persona = PersonaResolver.resolve(c, s, g)
            assertEquals(c, persona.context)
            assertEquals(s, persona.sex)
            assertEquals(g, persona.goal)
            count++
        }
        // 3 × 2 × 3 = 18 cells (includes both "bulk/cut/general_fit")
        assertEquals(18, count)
    }

    @Test
    fun `cellId matches Postgres persona_cell format`() {
        val persona = PersonaResolver.resolve(
            TrainingContext.SERIOUS_GYM, Sex.MALE, GoalType.BULK
        )
        assertEquals("serious_gym.male.bulk", persona.cellId)
    }

    @Test
    fun `cellId female cut casual_gym`() {
        val persona = PersonaResolver.resolve(
            TrainingContext.CASUAL_GYM, Sex.FEMALE, GoalType.CUT
        )
        assertEquals("casual_gym.female.cut", persona.cellId)
    }
}
