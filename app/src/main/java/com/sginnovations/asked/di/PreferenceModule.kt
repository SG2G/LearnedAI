package com.sginnovations.asked.di

import android.content.Context
import com.sginnovations.asked.data.preferences.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object PreferenceModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object PreferencesModule {

        @Singleton
        @Provides
        fun providePreferences(@ApplicationContext app: Context): Preferences = Preferences(app)
    }
}