package com.sginnovations.asked.di

import com.sginnovations.asked.data.network.CloudService
import com.sginnovations.asked.domain.repository.ArticlesRepository
import com.sginnovations.asked.data.repository.ArticlesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideArticlesRepository(cloudService: CloudService): ArticlesRepository =
        ArticlesRepositoryImpl(cloudService)
}
