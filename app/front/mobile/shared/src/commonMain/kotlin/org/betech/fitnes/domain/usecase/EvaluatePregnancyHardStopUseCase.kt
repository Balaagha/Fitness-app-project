package org.betech.fitnes.domain.usecase

import org.betech.fitnes.domain.model.PregnancyPostpartumStatus

/**
 * Per `prd-pregnancy-postpartum-flow-2026-05-23.md` + project-context §6.3:
 * `PREGNANT` and `POSTPARTUM_LT_6M` → AI plan generation MUST be blocked;
 * a curated static template is shown with medical disclaimer.
 * `POSTPARTUM_GT_6M` and `NONE` → no block.
 *
 * Deterministic, no randomness, no AI runtime trigger.
 */
class EvaluatePregnancyHardStopUseCase {
    operator fun invoke(status: PregnancyPostpartumStatus): HardStopResult = when (status) {
        PregnancyPostpartumStatus.PREGNANT,
        PregnancyPostpartumStatus.POSTPARTUM_LT_6M ->
            HardStopResult.Block(
                titleKey = "pregnancy.hardstop.title",
                bodyKey = "pregnancy.hardstop.body"
            )
        PregnancyPostpartumStatus.POSTPARTUM_GT_6M,
        PregnancyPostpartumStatus.NONE ->
            HardStopResult.NoBlock
    }
}

sealed interface HardStopResult {
    data object NoBlock : HardStopResult
    data class Block(val titleKey: String, val bodyKey: String) : HardStopResult
}
