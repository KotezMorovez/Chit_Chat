package com.example.chit_chat.data.profile.dto.chat

import com.example.chit_chat.domain.profile.dto.Chat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.joda.time.DateTime

fun QuerySnapshot.toChatEntityList(): List<ChatEntity> {
    return this.documents.map { it.toChatEntity() }
}

fun DocumentSnapshot.toChatEntity(): ChatEntity {
    return ChatEntity(
        id = this.id,
        chatName = this["chatName"] as String?,
        lastMessage = this["lastMessage"] as String,
        lastMessageDate = this["lastMessageDate"] as Long,
        chatAvatar = this["chatAvatar"] as String?,
        userIdList = this["userIdList"] as ArrayList<String>
    )
}

fun ChatEntity.toDomain(): Chat {
    val dateTime = DateTime(this.lastMessageDate)

    return Chat(
        id = this.id,
        chatName = this.chatName ?: "",
        lastMessage = this.lastMessage,
        lastMessageDate = dateTime,
        userIdList = this.userIdList,
        chatAvatar = this.chatAvatar
    )
}

fun Chat.toEntity(): ChatEntity {
    val date = this.lastMessageDate.millis

    return ChatEntity(
        id = this.id,
        chatName = this.chatName,
        lastMessage = this.lastMessage,
        lastMessageDate = date,
        userIdList = this.userIdList,
        chatAvatar = this.chatAvatar,
    )
}