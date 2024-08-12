package com.example.chit_chat.data.mapper

import com.example.chit_chat.data.model.ChatEntity
import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.domain.model.Chat
import com.example.chit_chat.domain.model.Profile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.joda.time.DateTime

fun DocumentSnapshot.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this["email"] as String,
        avatar = this["avatar"] as String,
        firstName = this["firstName"] as String,
        lastName = this["lastName"] as String,
    )
}

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

fun ProfileEntity.toDocument(): Map<String, Any> {
    return mapOf(
        "email" to this.email,
        "avatar" to (this.avatar ?: ""),
        "firstName" to this.firstName,
        "lastName" to this.lastName
    )
}

fun ProfileEntity.toDomain(): Profile {
    return Profile(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
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

fun Profile.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}