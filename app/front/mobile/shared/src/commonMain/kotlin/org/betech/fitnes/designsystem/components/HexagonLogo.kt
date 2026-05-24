package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Stylised hexagon outline with a barbell-"H" glyph inside. Pure Canvas — no
 * raster asset dependency, so it ships with the binary for free.
 *
 * Shared across Splash (volt bg, onVolt glyph) and Welcome (surface0 bg,
 * volt-filled hexagon with onVolt glyph). The optional `fillColor` paints the
 * hexagon interior; `null` keeps it transparent (stroke-only outline).
 */
@Composable
fun HexagonLogo(
    size: Dp,
    color: Color,
    modifier: Modifier = Modifier,
    fillColor: Color? = null,
    strokeDp: Dp = 3.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val sizePx = this.size.minDimension
        val strokePx = strokeDp.toPx()
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val r = sizePx / 2f - 4.dp.toPx()

        val path = Path().apply {
            for (i in 0..5) {
                val angle = ((60.0 * i - 90.0) * kotlin.math.PI / 180.0).toFloat()
                val x = cx + r * cos(angle)
                val y = cy + r * sin(angle)
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }

        if (fillColor != null) {
            drawPath(path = path, color = fillColor)
        }
        drawPath(path = path, color = color, style = Stroke(width = strokePx))

        // Barbell "H" — middle bar
        val barW = r * 0.7f
        val barH = 3.dp.toPx()
        drawRect(
            color = color,
            topLeft = Offset(cx - barW / 2f, cy - barH / 2f),
            size = Size(barW, barH),
        )
        // Side weights
        val sideH = r * 0.55f
        val sideW = 3.dp.toPx()
        drawRect(
            color = color,
            topLeft = Offset(cx - barW / 2f - 1.dp.toPx(), cy - sideH / 2f),
            size = Size(sideW, sideH),
        )
        drawRect(
            color = color,
            topLeft = Offset(cx + barW / 2f - sideW + 1.dp.toPx(), cy - sideH / 2f),
            size = Size(sideW, sideH),
        )
    }
}
