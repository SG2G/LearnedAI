package com.sginnovations.asked.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sginnovations.asked.data.database.dao.ChatDao
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.data.database.entities.MessageEntity

@Database(entities = [ConversationEntity::class, MessageEntity::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun getChatDao(): ChatDao

}