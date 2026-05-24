package org.betech.fitnes.domain.repository

import org.betech.fitnes.domain.model.AnalyticsEvent

interface AnalyticsRepository {
    suspend fun track(event: AnalyticsEvent)
}
