package ru.skypaws.mobileapp.data.datasource.local.user.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.skypaws.data.UserOuterClass.User
import ru.skypaws.mobileapp.data.datasource.local.user.UserLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.user.userProtoDataStore
import ru.skypaws.mobileapp.data.model.dto.UserDto
import ru.skypaws.mobileapp.domain.model.UserDomain
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserLocalDataSource {
    /**
     * Gets user [accessToken][UserDto.access_token] from local storage
     */
    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        context.userProtoDataStore.data.map { user ->
            user.accessToken.ifEmpty { null }
        }.first()
    }

    override suspend fun getApiKey(): String? = withContext(Dispatchers.IO) {
        context.userProtoDataStore.data.map { user ->
            user.apikey.ifEmpty { null }
        }.first()
    }

    /**
     * Gets [UserDomain] from local storage
     */
    override suspend fun getLocalUser(): UserDomain {
        return context.userProtoDataStore.data.map { user ->
            UserDomain(
                id = user.id.ifEmpty { null },
                name = user.name.ifEmpty { null },
                surname = user.surname.ifEmpty { null },
                position = user.position.ifEmpty { null },
                airline = user.airline,
                company = user.company.ifEmpty { null },
                photo = user.photo.ifEmpty { null },
                isActive = user.isActive,
                isSuperuser = user.isSuperuser,
                isVerified = user.isVerified,
                role = user.role
            )
        }.first()
    }

    /**
     * Totally clears all user data from local storage
     */
    override suspend fun clearUserData() {
        context.userProtoDataStore.updateData {
            User.newBuilder().build()
        }
    }

    /**
     * Saves [UserDto] to local storage
     */
    override suspend fun saveUser(user: UserDto) {
        context.userProtoDataStore.updateData { currentUser ->
            currentUser.toBuilder()
                .setId(user.id)
                .setName(user.name)
                .setSurname(user.surname)
                .setPosition(user.position)
                .setAirline(user.airline)
                .setCompany(user.company)
                .setPhoto(user.photo)
                .setIsActive(user.is_active)
                .setIsSuperuser(user.is_superuser)
                .setIsVerified(user.is_verified)
                .setRole(user.role)
                .setApikey(user.apikey)
                .setAccessToken(user.access_token)
                .setRefreshToken(user.refresh_token)
                .build()
        }
    }

    /**
     * Updates [UserDto] in local storage
     */
    override suspend fun updateUser(user: UserDto) {
        context.userProtoDataStore.updateData { currentUser ->
            val builder = currentUser.toBuilder()

            user.id?.let { builder.setId(it) }
            user.name?.let { builder.setName(it) }
            user.surname?.let { builder.setSurname(it) }
            user.position?.let { builder.setPosition(it) }
            builder.setAirline(user.airline)
            user.company?.let { builder.setCompany(it) }
            user.photo?.let { builder.setPhoto(it) }
            builder.setIsActive(user.is_active)
            builder.setIsSuperuser(user.is_superuser)
            builder.setIsVerified(user.is_verified)
            builder.setRole(user.role)
            user.apikey?.let { builder.setApikey(it) }
            user.access_token?.let { builder.setAccessToken(it) }
            user.refresh_token?.let { builder.setRefreshToken(it) }

            builder.build()
        }
    }
}