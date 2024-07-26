package com.example.chit_chat.data.mapper

import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.domain.model.Profile
import com.google.firebase.firestore.DocumentSnapshot

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

fun DocumentSnapshot.toEntity() : ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this["email"] as String,
        avatar = this["avatar"] as String,
        firstName = this["firstName"] as String,
        lastName = this["lastName"] as String,
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