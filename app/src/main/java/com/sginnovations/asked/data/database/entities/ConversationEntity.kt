package com.sginnovations.asked.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sginnovations.asked.Constants.Companion.CONVERSATION_TABLE_NAME

@Entity(tableName = CONVERSATION_TABLE_NAME)
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val idConversation: Int? = null,
    val name: String,
    val category: String,
    val visible: Boolean,
)