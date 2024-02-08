package com.sginnovations.asked.di

import com.sginnovations.asked.data.repository.ArticlesRepositoryImpl
import com.sginnovations.asked.domain.repository.ArticlesRepository
import com.sginnovations.asked.domain.usecase.GetArticlesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

//@Module
//@InstallIn(ViewModelComponent::class)
//object ViewModelModule {
//
//    @Provides
//    fun provideGetArticlesUseCase(repository: ArticlesRepository): GetArticlesUseCase =
//        GetArticlesUseCase(repository)
//}
