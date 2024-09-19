package com.example.chit_chat.data.firebase

import com.example.chit_chat.data.profile.dto.chat.toChatEntityList
import com.example.chit_chat.data.profile.dto.profile.toDocument
import com.example.chit_chat.data.profile.dto.profile.toEntity
import com.example.chit_chat.data.profile.dto.chat.ChatEntity
import com.example.chit_chat.data.profile.dto.profile.ProfileEntity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface FirebaseService {
    suspend fun getProfileById(id: String): Result<ProfileEntity>
    suspend fun saveProfile(user: ProfileEntity): Result<Unit>
    suspend fun observeChatList(id: String): Flow<List<ChatEntity>>
    suspend fun deleteChatGlobally(chatId: String): Result<Unit>
    suspend fun updateChatParticipants(
        userIdList: ArrayList<String>,
        chatId: String
    ): Result<Unit>
}

class FirebaseServiceImpl @Inject constructor() : FirebaseService {
    private val database = Firebase.firestore
    private val profileCollection = database.collection(PROFILES_COLLECTION)
    private val chatCollection = database.collection(CHATS_COLLECTION)

    override suspend fun getProfileById(id: String): Result<ProfileEntity> {
        return suspendCoroutine { continuation ->
            profileCollection
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

    override suspend fun observeChatList(id: String): Flow<List<ChatEntity>> {
        val flow = MutableSharedFlow<List<ChatEntity>>(1)

        val subscription = chatCollection
            .whereArrayContains("userIdList", id)
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    val chatList = value.toChatEntityList()
                    runBlocking {
                        flow.emit(chatList)
                    }
                }
            }

        return flow.onCompletion { subscription.remove() }
    }

    override suspend fun deleteChatGlobally(
        chatId: String
    ): Result<Unit> {
        return suspendCoroutine { continuation ->
            chatCollection
                .document(chatId)
                .delete()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun updateChatParticipants(
        userIdList: ArrayList<String>,
        chatId: String,
    ): Result<Unit> {
        return suspendCoroutine { continuation ->
            chatCollection
                .document(chatId)
                .update(mapOf("userIdList" to userIdList))
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.success(Result.failure(it)))
                }
        }
    }

    override suspend fun saveProfile(user: ProfileEntity): Result<Unit> {
        return suspendCoroutine { continuation ->
            profileCollection
                .document(user.id)
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
        private const val CHATS_COLLECTION = "chats"
    }
}