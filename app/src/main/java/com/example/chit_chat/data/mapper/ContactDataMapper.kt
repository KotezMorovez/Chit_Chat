package com.example.chit_chat.data.mapper

import com.example.chit_chat.data.model.ContactEntity
import com.example.chit_chat.domain.model.Contact
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toContactEntity(): ContactEntity {
    return ContactEntity(
        id = this.id,
        avatar = this["avatar"] as String,
        firstName = this["firstName"] as String,
        lastName = this["lastName"] as String
    )
}

fun ContactEntity.toDomain(): Contact {
    return Contact(
        id = this.id,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}