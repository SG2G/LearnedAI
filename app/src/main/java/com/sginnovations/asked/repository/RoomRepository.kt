package com.sginnovations.asked.repository

import com.sginnovations.asked.data.database.dao.ChatDao
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.data.database.entities.MessageEntity
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val chatDao: ChatDao
) {

    suspend fun createConversation(conversationEntity: ConversationEntity): Long {
        return chatDao.insertConversation(conversationEntity)
    }

    suspend fun insertMessage(messageEntity: MessageEntity) {
        chatDao.insertMessage(messageEntity)
    }

    suspend fun getAllMessages(id: Int): List<MessageEntity> {
        return chatDao.getConversationMessages(id)
    }

    suspend fun getAllConversations(): List<ConversationEntity> {
        return chatDao.getAllConversations()
    }
    suspend fun hideConversation(id: Int){
        chatDao.hideConversation(id)
    }

}