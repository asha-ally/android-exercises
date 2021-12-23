package com.test.movies_test.modules

import com.test.movies_test.data.remote.ApiService
import com.test.movies_test.data.repository.AppRepository
import com.test.movies_test.data.repository.AppRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Singleton

@DelicateCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): AppRepository {
        return AppRepositoryImpl(apiService)
    }
}