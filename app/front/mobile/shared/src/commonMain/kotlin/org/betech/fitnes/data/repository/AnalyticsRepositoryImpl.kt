package org.betech.fitnes.data.repository

import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.betech.fitnes.data.dto.AnalyticsEventDto
import org.betech.fitnes.data.source.remote.RemoteSource
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository

/**
 * No-op analytics — prints to stdout + forwards to remote.
 * Real backend (PostHog / Supabase logs) wires in via [RemoteSource] later.
 */
@OptIn(ExperimentalTime::class)
class AnalyticsRepositoryImpl(
    private val remote: RemoteSource,
    private val json: Json
) : AnalyticsRepository {
    override suspend fun track(event: AnalyticsEvent) {
        println("analytics: ${event::class.simpleName} -> ${event.name}")
        val dto = AnalyticsEventDto(event = event, emittedAt = nowIso())
        val raw = json.encodeToString(AnalyticsEventDto.serializer(), dto)
        remote.emitAnalytics(raw)
    }

    private fun nowIso(): String = Clock.System.now().toString()
}
