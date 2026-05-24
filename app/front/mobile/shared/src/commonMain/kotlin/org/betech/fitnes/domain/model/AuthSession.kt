package org.betech.fitnes.domain.model

import kotlinx.datetime.Instant

/** Pure domain — wire serialization lives in `AuthSessionDto`. */
data class AuthSession(
    val userId: String,
    val email: String?,
    val method: AuthMethod,
    val issuedAt: Instant,
    val accessTokenPreview: String? = null
)
