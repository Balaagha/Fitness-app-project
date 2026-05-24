package org.betech.fitnes.data.dto

import kotlinx.serialization.Serializable
import org.betech.fitnes.domain.model.AuthMethod

@Serializable
data class AuthSessionDto(
    val userId: String,
    val email: String? = null,
    val method: AuthMethod,
    val issuedAt: String,
    val accessTokenPreview: String? = null
)
