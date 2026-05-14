package com.apkidemand.feature.home.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getDemandsFlow(category: String = "All"): Flow<List<DemandEntity>> = callbackFlow {
        var query: Query = firestore.collection("demands")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            
        if (category != "All") {
            query = query.whereEqualTo("category", category)
        }

        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val demands = snapshot.documents.mapNotNull { it.toObject(DemandEntity::class.java)?.copy(id = it.id) }
                trySend(demands)
            }
        }

        awaitClose { subscription.remove() }
    }

    suspend fun postDemand(demand: DemandEntity): Result<Unit> {
        return try {
            val docRef = firestore.collection("demands").document()
            val demandWithId = demand.copy(id = docRef.id)
            docRef.set(demandWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getOffersForDemand(demandId: String): Flow<List<OfferEntity>> = callbackFlow {
        val query = firestore.collection("offers")
            .whereEqualTo("demandId", demandId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val offers = snapshot.documents.mapNotNull { it.toObject(OfferEntity::class.java)?.copy(id = it.id) }
                trySend(offers)
            }
        }

        awaitClose { subscription.remove() }
    }

    fun getOffersFlow(category: String = "All"): Flow<List<OfferEntity>> = callbackFlow {
        var query: Query = firestore.collection("offers")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            
        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val offers = snapshot.documents.mapNotNull { it.toObject(OfferEntity::class.java)?.copy(id = it.id) }
                trySend(offers)
            }
        }

        awaitClose { subscription.remove() }
    }

    suspend fun submitOffer(offer: OfferEntity): Result<Unit> {
        return try {
            val docRef = firestore.collection("offers").document()
            val offerWithId = offer.copy(id = docRef.id)
            docRef.set(offerWithId).await()
            
            // Increment offer count on demand
            firestore.collection("demands").document(offer.demandId)
                .update("offerCount", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
                
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserDemands(userId: String): Flow<List<DemandEntity>> = callbackFlow {
        val query = firestore.collection("demands")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val demands = snapshot.documents.mapNotNull { it.toObject(DemandEntity::class.java)?.copy(id = it.id) }
                trySend(demands)
            }
        }

        awaitClose { subscription.remove() }
    }

    fun getUserOffers(userId: String): Flow<List<OfferEntity>> = callbackFlow {
        val query = firestore.collection("offers")
            .whereEqualTo("sellerId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val offers = snapshot.documents.mapNotNull { it.toObject(OfferEntity::class.java)?.copy(id = it.id) }
                trySend(offers)
            }
        }

        awaitClose { subscription.remove() }
    }
}
