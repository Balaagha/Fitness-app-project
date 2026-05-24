package org.betech.fitnes.data.mapper

import kotlinx.datetime.Instant
import org.betech.fitnes.data.dto.AuthSessionDto
import org.betech.fitnes.domain.model.AuthSession

fun AuthSessionDto.toDomain(): AuthSession = AuthSession(
    userId = userId,
    email = email,
    method = method,
    issuedAt = runCatching { Instant.parse(issuedAt) }
        .getOrDefault(Instant.fromEpochMilliseconds(0)),
    accessTokenPreview = accessTokenPreview
)

fun AuthSession.toDto(): AuthSessionDto = AuthSessionDto(
    userId = userId,
    email = email,
    method = method,
    issuedAt = issuedAt.toString(),
    accessTokenPreview = accessTokenPreview
)
