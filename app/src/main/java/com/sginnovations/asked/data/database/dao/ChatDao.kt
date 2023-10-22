package com.sginnovations.asked.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sginnovations.asked.Constants.Companion.CONVERSATION_TABLE_NAME
import com.sginnovations.asked.Constants.Companion.MESSAGE_TABLE_NAME
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.data.database.entities.MessageEntity

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM $CONVERSATION_TABLE_NAME")
    suspend fun getAllConversations(): List<ConversationEntity>

    @Query("SELECT * FROM $MESSAGE_TABLE_NAME WHERE idConversation = :idConversation")
    suspend fun getConversationMessages(idConversation: Int): List<MessageEntity>

    @Query("UPDATE $CONVERSATION_TABLE_NAME SET visible = 0 WHERE idConversation = :idConversation")
    suspend fun hideConversation(idConversation: Int)

    // More testing
    @Query("SELECT * FROM $MESSAGE_TABLE_NAME")
    suspend fun getAll(): List<MessageEntity>


}