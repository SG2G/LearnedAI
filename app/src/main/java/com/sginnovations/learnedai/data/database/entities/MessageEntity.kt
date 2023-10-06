package com.sginnovations.learnedai.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sginnovations.learnedai.Constants.Companion.MESSAGE_TABLE_NAME

@Entity(tableName = MESSAGE_TABLE_NAME)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val idMessage: Int? = null,
    val idConversation: Int,
    val role: String,
    val content: String,
    val timestamp: Long,
)