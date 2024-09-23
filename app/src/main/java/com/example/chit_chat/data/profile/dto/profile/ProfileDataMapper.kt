package com.example.chit_chat.data.profile.dto.profile

import com.example.chit_chat.domain.profile.dto.Profile
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this["email"] as String,
        avatar = this["avatar"] as String,
        firstName = this["firstName"] as String,
        lastName = this["lastName"] as String
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

fun Profile.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}