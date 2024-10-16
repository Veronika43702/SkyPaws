package ru.skypaws.mobileapp.data.di.repository.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.user.UserPayInfoRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.user.UserPayInfoRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPayInfoRepositoryModule {

    @Binds
    abstract fun bindUserPayInfoRepository(
        userPayInfoRepositoryImpl: UserPayInfoRepositoryImpl
    ): UserPayInfoRepository
}