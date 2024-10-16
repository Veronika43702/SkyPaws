package ru.skypaws.data.di.utils

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.utils.DateUtils
import ru.skypaws.data.utils.DateUtilsImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DateUtilsModule {

    @Binds
    abstract fun bindDateUtils(dateUtilsImpl: DateUtilsImpl): DateUtils
}