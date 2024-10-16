package ru.skypaws.data.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.skypaws.data.utils.keyLogbookPrice
import ru.skypaws.data.utils.keySyncMonthPrice
import ru.skypaws.data.utils.keySyncQuarterPrice
import ru.skypaws.data.utils.keySyncYearPrice
import ru.skypaws.data.utils.prefSettings
import javax.inject.Inject

class ServiceStorageSharedPrefs@Inject constructor(
    @ApplicationContext private val context: Context
): ServiceStorage {
    private val prefsSettings = context.getSharedPreferences(prefSettings, Context.MODE_PRIVATE)

    override fun savePriceInfo(
        logbookPrice: Int,
        syncMonthPrice: Int,
        syncQuarterPrice: Int,
        syncYearPrice: Int
    ) {
        prefsSettings.edit()
            .putInt(keyLogbookPrice, logbookPrice)
            .putInt(keySyncMonthPrice, syncMonthPrice)
            .putInt(keySyncQuarterPrice, syncQuarterPrice)
            .putInt(keySyncYearPrice, syncYearPrice)
            .apply()
    }

    override fun getLogbookPrice(): Int = prefsSettings.getInt(keyLogbookPrice, 0)

    override fun getCalendarMonthPrice(): Int = prefsSettings.getInt(keySyncMonthPrice, 0)
    override fun getCalendarQuarterPrice(): Int = prefsSettings.getInt(keySyncQuarterPrice, 0)
    override fun getCalendarYearPrice(): Int = prefsSettings.getInt(keySyncYearPrice, 0)
}