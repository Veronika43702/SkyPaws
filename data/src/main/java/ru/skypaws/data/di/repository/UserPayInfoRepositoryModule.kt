package ru.skypaws.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.repository.UserPayInfoRepositoryImpl
import ru.skypaws.domain.repository.UserPayInfoRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPayInfoRepositoryModule {

    @Binds
    abstract fun bindUserPayInfoRepository(
        userPayInfoRepositoryImpl: UserPayInfoRepositoryImpl
    ): UserPayInfoRepository
}