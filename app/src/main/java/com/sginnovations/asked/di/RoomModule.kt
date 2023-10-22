package com.sginnovations.asked.di

import android.content.Context
import androidx.room.Room
import com.sginnovations.asked.Constants.Companion.MESSAGE_TABLE_NAME
import com.sginnovations.asked.data.database.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ChatDatabase::class.java, MESSAGE_TABLE_NAME).build()

    @Singleton
    @Provides
    fun provideNoteDao(db: ChatDatabase) = db.getChatDao()

}