package org.betech.fitnes.presentation.onboarding.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors

/**
 * Step-progress descriptor used by [QuestionProgressBar].
 *
 * @param current 1-based index of the current step (must be in 1..total).
 * @param total total number of steps; defaults to 7 (the mandatory Q1–Q7 spine).
 */
data class QuestionProgress(val current: Int, val total: Int = 7) {
    init {
        require(current in 1..total) { "current must be in 1..$total" }
    }
}

/**
 * Step-progress bar shared across the 7 mandatory onboarding questions
 * (Q1–Q7). Renders `total` equal-weight 6dp-tall pills with the first
 * `current` segments filled `volt`; remaining segments are tinted with `outline`.
 *
 * @param progress current/total descriptor; see [QuestionProgress].
 * @param modifier applied to the outer [Row]. Caller controls width; pills
 *                 stretch to fill the available row.
 */
@Composable
fun QuestionProgressBar(
    progress: QuestionProgress,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(progress.total) { i ->
            val filled = i < progress.current
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (filled) VoltColors.volt else VoltColors.outline),
            )
        }
    }
}
