package com.sginnovations.asked.di

import android.content.Context
import com.sginnovations.asked.data.lessons.LessonDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LessonsContextModule {

    @Singleton
    @Provides
    fun provideLessonDataSource(@ApplicationContext context: Context): LessonDataSource {
        return LessonDataSource(context)
    }

    // ... other dependencies
}
