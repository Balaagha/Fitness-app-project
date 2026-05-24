package org.betech.fitnes.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.betech.fitnes.data.dto.UserProfileDto
import org.betech.fitnes.data.mapper.toDomain
import org.betech.fitnes.data.mapper.toDto
import org.betech.fitnes.data.source.remote.RemoteSource
import org.betech.fitnes.domain.model.UserProfile
import org.betech.fitnes.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val remote: RemoteSource,
    private val json: Json
) : UserProfileRepository {

    private val cache = MutableStateFlow<UserProfile?>(null)

    // Repo-local state for preferred language until UserProfile schema adds the field.
    // Default "az" — primary locale per CLAUDE.md.
    private val preferredLanguage = MutableStateFlow("az")

    override suspend fun getProfile(): UserProfile? {
        val raw = remote.fetchProfileJson() ?: return null
        val domain = json.decodeFromString<UserProfileDto>(raw).toDomain()
        cache.value = domain
        return domain
    }

    override suspend fun saveProfile(profile: UserProfile) {
        val raw = json.encodeToString(UserProfileDto.serializer(), profile.toDto())
        remote.saveProfileJson(raw)
        cache.value = profile
    }

    override fun observeProfile(): Flow<UserProfile?> = cache.asStateFlow()

    override suspend fun setPreferredLanguage(code: String) {
        preferredLanguage.value = code.lowercase().take(2)
    }

    override fun observePreferredLanguage(): Flow<String> = preferredLanguage.asStateFlow()
}
