package org.betech.fitnes.domain.repository

import kotlinx.coroutines.flow.Flow
import org.betech.fitnes.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getProfile(): UserProfile?
    suspend fun saveProfile(profile: UserProfile)
    fun observeProfile(): Flow<UserProfile?>

    /**
     * Persist the user's preferred UI language (ISO 2-letter: "az" / "ru" / "en").
     * TODO: when UserProfile gains a `preferredLanguage` field, fold this into the
     * profile DTO + remote sync. For now stored in repo-local state only.
     */
    suspend fun setPreferredLanguage(code: String)
    fun observePreferredLanguage(): Flow<String>
}
