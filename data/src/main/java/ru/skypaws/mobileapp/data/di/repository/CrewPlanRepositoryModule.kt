package ru.skypaws.mobileapp.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.CrewPlanRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.CrewPlanRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class CrewPlanRepositoryModule {

    @Binds
    abstract fun bindCrewPlanRepository(
        crewPlanRepositoryImpl: CrewPlanRepositoryImpl
    ): CrewPlanRepository
}