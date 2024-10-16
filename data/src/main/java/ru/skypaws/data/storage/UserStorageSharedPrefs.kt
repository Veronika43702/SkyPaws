package ru.skypaws.data.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.skypaws.data.source.dto.PayInfoDto
import ru.skypaws.data.source.dto.UserDto
import ru.skypaws.data.utils.keyAccessToken
import ru.skypaws.data.utils.keyAirline
import ru.skypaws.data.utils.keyApikey
import ru.skypaws.data.utils.keyCompany
import ru.skypaws.data.utils.keyId
import ru.skypaws.data.utils.keyIsActive
import ru.skypaws.data.utils.keyIsSuperuser
import ru.skypaws.data.utils.keyIsVerified
import ru.skypaws.data.utils.keyLogbookExpDate
import ru.skypaws.data.utils.keyName
import ru.skypaws.data.utils.keyPhoto
import ru.skypaws.data.utils.keyPosition
import ru.skypaws.data.utils.keyRefreshToken
import ru.skypaws.data.utils.keyRole
import ru.skypaws.data.utils.keySurname
import ru.skypaws.data.utils.keySyncExpDate
import ru.skypaws.data.utils.keyTrialUntil
import ru.skypaws.data.utils.prefAuth
import javax.inject.Inject

class UserStorageSharedPrefs @Inject constructor(
    @ApplicationContext private val context: Context
): UserStorage {
    private val prefsAuth = context.getSharedPreferences(prefAuth, Context.MODE_PRIVATE)

    override fun getAccessToken(): String? = prefsAuth.getString(keyAccessToken, null)

    /**
     * gets [UserDto] from SharedPreferences
     */
    override fun getLocalUser(): UserDto {
        return UserDto(
            id = prefsAuth.getString(keyId, "0"),
            role = prefsAuth.getInt(keyRole, -1),
            name = prefsAuth.getString(keyName, null),
            surname = prefsAuth.getString(keySurname, null),
            position = prefsAuth.getString(keyPosition, null),
            airline = prefsAuth.getInt(keyAirline, 1),
            company = prefsAuth.getString(keyCompany, null),
            photo = prefsAuth.getString(keyPhoto, null),
            is_active = prefsAuth.getBoolean(keyIsActive, false),
            is_superuser = prefsAuth.getBoolean(keyIsSuperuser, false),
            is_verified = prefsAuth.getBoolean(keyIsVerified, false),
        )
    }

    /**
     * totally clears all user data from prefAuth
     */
    override fun clearUserData() {
        with(prefsAuth.edit()) {
            clear()
            commit()
        }
    }

    /**
     * saves [UserDto] to SharedPreferences
     */
    override fun saveUser(user: UserDto) {
        with(prefsAuth.edit()) {
            putString(keyId, user.id)
            putString(keyAccessToken, user.access_token)
            putString(keyRefreshToken, user.refresh_token)
            putString(keyApikey, user.apikey)
            putInt(keyRole, user.role)
            putString(keyName, user.name)
            putString(keySurname, user.surname)
            putString(keyPosition, user.position)
            putInt(keyAirline, user.airline)
            putString(keyCompany, user.company)
            putString(keyPhoto, user.photo)
            putBoolean(keyIsActive, user.is_active)
            putBoolean(keyIsVerified, user.is_verified)
            putBoolean(keyIsSuperuser, user.is_superuser)
            apply()
        }
    }

    /**
     * updates [UserDto] in SharedPreferences (apiKey, isActive, isVerified, IsSuperuser)
     */
    override fun updateUser(user: UserDto) {
        with(prefsAuth.edit()) {
            putString(keyId, user.id)
            putString(keyApikey, user.apikey)
            putInt(keyRole, user.role)
            putString(keyName, user.name)
            putString(keySurname, user.surname)
            putString(keyPosition, user.position)
            putInt(keyAirline, user.airline)
            putString(keyCompany, user.company)
            putBoolean(keyIsActive, user.is_active)
            putBoolean(keyIsVerified, user.is_verified)
            putBoolean(keyIsSuperuser, user.is_superuser)
            apply()
        }
    }

    /**
     * saves [PayInfoDto] in SharedPreferences
     */
    override fun savePayInfo(payInfoDto: PayInfoDto) {
        prefsAuth.edit()
            .putLong(keyTrialUntil, payInfoDto.trial_until)
            .putLong(keyLogbookExpDate, payInfoDto.logbook_exp)
            .putLong(keySyncExpDate, payInfoDto.sync_exp)
            .apply()
    }

    /**
     * gets Logbook Expiration Date in SharedPreferences
     */
    override fun getLogbookExpDate(): Long = prefsAuth.getLong(keyLogbookExpDate, 0L)
    /**
     * gets Calendar Expiration Date in SharedPreferences
     */
    override fun getCalendarExpDate(): Long = prefsAuth.getLong(keySyncExpDate, 0L)
}