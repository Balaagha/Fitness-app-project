package org.betech.fitnes

import org.betech.fitnes.domain.model.PregnancyPostpartumStatus
import org.betech.fitnes.domain.usecase.EvaluatePregnancyHardStopUseCase
import org.betech.fitnes.domain.usecase.HardStopResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PregnancyHardStopTest {

    private val useCase = EvaluatePregnancyHardStopUseCase()

    @Test
    fun `pregnant blocks`() {
        assertTrue(useCase(PregnancyPostpartumStatus.PREGNANT) is HardStopResult.Block)
    }

    @Test
    fun `postpartum lt 6m blocks`() {
        assertTrue(useCase(PregnancyPostpartumStatus.POSTPARTUM_LT_6M) is HardStopResult.Block)
    }

    @Test
    fun `postpartum gt 6m does not block`() {
        assertEquals(HardStopResult.NoBlock, useCase(PregnancyPostpartumStatus.POSTPARTUM_GT_6M))
    }

    @Test
    fun `none does not block`() {
        assertEquals(HardStopResult.NoBlock, useCase(PregnancyPostpartumStatus.NONE))
    }
}
