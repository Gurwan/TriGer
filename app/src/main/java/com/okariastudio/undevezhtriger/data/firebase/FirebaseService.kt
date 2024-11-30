package com.okariastudio.undevezhtriger.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.okariastudio.undevezhtriger.data.model.Ger
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseService {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchAllGeriou(): List<Ger> = suspendCoroutine { continuation ->
        firestore.collection("gerioù")
            .get()
            .addOnSuccessListener { documents ->
                val gerList = documents.mapNotNull { doc ->
                    doc.toObject(Ger::class.java).copy(id = doc.id)
                }
                continuation.resume(gerList)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}
