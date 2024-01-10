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

    /**
     * Conversations
     */
    @Query("SELECT * FROM $CONVERSATION_TABLE_NAME WHERE category <> 'Assistant'")
    suspend fun getAllConversationsExceptAssistant(): List<ConversationEntity>

    @Query("SELECT * FROM $CONVERSATION_TABLE_NAME WHERE category = :category")
    suspend fun getCategoryConversations(category: String): List<ConversationEntity>

    /**
     * Messages
     */
    @Query("SELECT * FROM $MESSAGE_TABLE_NAME WHERE idConversation = :idConversation")
    suspend fun getConversationMessages(idConversation: Int): List<MessageEntity>

    @Query("UPDATE $CONVERSATION_TABLE_NAME SET visible = 0 WHERE idConversation = :idConversation")
    suspend fun hideConversation(idConversation: Int)

    @Query("DELETE FROM $CONVERSATION_TABLE_NAME WHERE idConversation = :idConversation")
    suspend fun deleteConversation(idConversation: Int)


    // More testing
    @Query("SELECT * FROM $MESSAGE_TABLE_NAME")
    suspend fun getAll(): List<MessageEntity>


}