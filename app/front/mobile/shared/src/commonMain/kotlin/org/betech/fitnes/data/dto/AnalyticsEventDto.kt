package org.betech.fitnes.data.dto

import kotlinx.serialization.Serializable
import org.betech.fitnes.domain.model.AnalyticsEvent

/** Wire DTO for analytics — wraps the sealed AnalyticsEvent for emit payload. */
@Serializable
data class AnalyticsEventDto(
    val event: AnalyticsEvent,
    val emittedAt: String
)
