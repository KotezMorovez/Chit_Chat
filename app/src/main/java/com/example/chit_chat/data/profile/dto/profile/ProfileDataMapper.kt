package com.example.chit_chat.data.profile.dto.profile

import com.example.chit_chat.domain.profile.dto.Profile
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this["email"] as String,
        avatar = this["avatar"] as String,
        firstName = this["firstName"] as String,
        lastName = this["lastName"] as String,
        contacts = this["contacts"] as List<String>
    )
}


fun ProfileEntity.toDocument(): Map<String, Any> {
    return mapOf(
        "email" to this.email,
        "avatar" to (this.avatar),
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
        lastName = this.lastName,
        contacts = this.contacts
    )
}

fun Profile.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName,
        contacts = this.contacts
    )
}