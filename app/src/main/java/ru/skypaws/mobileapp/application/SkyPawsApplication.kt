package ru.skypaws.mobileapp.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.skypaws.mobileapp.data.utils.dbVersion
import ru.skypaws.mobileapp.data.utils.version
import ru.skypaws.mobileapp.BuildConfig

@HiltAndroidApp
class SkyPawsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Delete all files in the cache directory during app initialization
        deleteAllFilesInCacheDir()

        version = BuildConfig.VERSION_NAME
        dbVersion = BuildConfig.DB_VERSION
    }

    private fun deleteAllFilesInCacheDir() {
        val cacheDir = cacheDir
        if (cacheDir.exists() && cacheDir.isDirectory) {
            cacheDir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }
}