package com.apkidemand.feature.home.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class ChatMessageEntity(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

data class ChatThreadEntity(
    val id: String = "", // e.g. "demandId_sellerId"
    val demandId: String = "",
    val buyerId: String = "",
    val sellerId: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Timestamp = Timestamp.now(),
    val buyerUnread: Int = 0,
    val sellerUnread: Int = 0
)

@Singleton
class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getChatThreads(userId: String, role: String): Flow<List<ChatThreadEntity>> = callbackFlow {
        val field = if (role == "buyer") "buyerId" else "sellerId"
        val query = firestore.collection("chats")
            .whereEqualTo(field, userId)
            .orderBy("lastTimestamp", Query.Direction.DESCENDING)

        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val threads = snapshot.documents.mapNotNull { it.toObject(ChatThreadEntity::class.java)?.copy(id = it.id) }
                trySend(threads)
            }
        }

        awaitClose { subscription.remove() }
    }

    fun getMessages(threadId: String): Flow<List<ChatMessageEntity>> = callbackFlow {
        val query = firestore.collection("chats").document(threadId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)

        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val messages = snapshot.documents.mapNotNull { it.toObject(ChatMessageEntity::class.java)?.copy(id = it.id) }
                trySend(messages)
            }
        }

        awaitClose { subscription.remove() }
    }

    suspend fun sendMessage(threadId: String, senderId: String, text: String): Result<Unit> {
        return try {
            val message = ChatMessageEntity(senderId = senderId, text = text)
            
            // Write message
            firestore.collection("chats").document(threadId).collection("messages").add(message).await()
            
            // Update thread lastMessage
            firestore.collection("chats").document(threadId).update(
                mapOf(
                    "lastMessage" to text,
                    "lastTimestamp" to Timestamp.now()
                )
            ).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
