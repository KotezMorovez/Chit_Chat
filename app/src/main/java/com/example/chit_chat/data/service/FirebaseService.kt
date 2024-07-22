package com.example.chit_chat.data.service

import com.example.chit_chat.data.mapper.toDocument
import com.example.chit_chat.data.mapper.toEntity
import com.example.chit_chat.data.model.ProfileEntity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


interface FirebaseService {
    suspend fun getProfileById(id: String): Result<ProfileEntity>
    suspend fun register(user: ProfileEntity, id: String): Result<Unit>
}

class FirebaseServiceImpl @Inject constructor() : FirebaseService {
    private val database = Firebase.firestore
    private val collection = database.collection(PROFILES_COLLECTION)

    override suspend fun getProfileById(id: String): Result<ProfileEntity> {
        return suspendCoroutine { continuation ->
            collection
                .document(id)
                .get()
                .addOnSuccessListener {
                    continuation.resumeWith(
                        Result.success(
                            Result.success(it.toEntity())
                        )
                    )
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun register(user: ProfileEntity, id: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            collection
                .document(id)
                .set(user.toDocument())
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    companion object {
        private const val PROFILES_COLLECTION = "profiles"
    }
}