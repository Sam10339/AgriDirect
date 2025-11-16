package com.example.agridirect20

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseFarmRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val farmsCollection = db.collection("farms")

    /**
     * Create a farm + its products in Firestore.
     */
    suspend fun createFarmWithProducts(
        name: String,
        description: String,
        zip: String,
        products: List<Product>
    ): Farm {
        val userId = AuthManager.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in to create a farm")

        val farmData = hashMapOf(
            "name" to name.trim(),
            "description" to description.trim(),
            "zip" to zip.trim(),
            "createdByUserId" to userId
        )

        val farmRef = farmsCollection.add(farmData).await()

        val productDocs = mutableListOf<Product>()
        for (product in products) {
            val productData = hashMapOf(
                "name" to product.name.trim(),
                "description" to product.description.trim(),
                "price" to product.price,
                "amount" to product.amount
            )
            val prodRef = farmRef.collection("products").add(productData).await()
            productDocs.add(product.copy(id = prodRef.id))
        }

        return Farm(
            id = farmRef.id,
            name = name.trim(),
            description = description.trim(),
            zip = zip.trim(),
            createdByUserId = userId,
            products = productDocs
        )
    }
    suspend fun getFarmsForCurrentUser(): List<Farm> {
        val userId = AuthManager.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in")

        val snapshot = farmsCollection
            .whereEqualTo("createdByUserId", userId)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Farm(
                id = doc.id,
                name = doc.getString("name") ?: "",
                description = doc.getString("description") ?: "",
                zip = doc.getString("zip") ?: "",
                createdByUserId = userId,
                products = emptyList() // we can load full details later
            )
        }
    }

    /**
     * Get all farms for a given ZIP code.
     */
    suspend fun getFarmsByZip(zip: String): List<Farm> {
        val snapshot = farmsCollection
            .whereEqualTo("zip", zip.trim())
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Farm(
                id = doc.id,
                name = doc.getString("name") ?: "",
                description = doc.getString("description") ?: "",
                zip = doc.getString("zip") ?: "",
                createdByUserId = doc.getString("createdByUserId") ?: "",
                products = emptyList() // details loaded separately if needed
            )
        }
    }

    /**
     * Get a single farm + all its products (for a farm details screen later).
     */
    suspend fun getFarmWithProducts(farmId: String): Farm? {
        val farmDoc = farmsCollection.document(farmId).get().await()
        if (!farmDoc.exists()) return null

        val name = farmDoc.getString("name") ?: ""
        val description = farmDoc.getString("description") ?: ""
        val zip = farmDoc.getString("zip") ?: ""
        val createdBy = farmDoc.getString("createdByUserId") ?: ""

        val productsSnapshot = farmDoc.reference
            .collection("products")
            .get()
            .await()

        val products = productsSnapshot.documents.map { prodDoc ->
            Product(
                id = prodDoc.id,
                name = prodDoc.getString("name") ?: "",
                description = prodDoc.getString("description") ?: "",
                price = prodDoc.getDouble("price") ?: 0.0,
                amount = (prodDoc.getLong("amount") ?: 0L).toInt()
            )
        }

        return Farm(
            id = farmDoc.id,
            name = name,
            description = description,
            zip = zip,
            createdByUserId = createdBy,
            products = products
        )
    }
    suspend fun updateFarm(
        farmId: String,
        name: String,
        description: String,
        zip: String
    ) {
        farmsCollection.document(farmId).update(
            mapOf(
                "name" to name.trim(),
                "description" to description.trim(),
                "zip" to zip.trim()
            )
        ).await()
    }
    suspend fun updateProduct(
        farmId: String,
        product: Product
    ) {
        farmsCollection
            .document(farmId)
            .collection("products")
            .document(product.id)
            .update(
                mapOf(
                    "name" to product.name,
                    "description" to product.description,
                    "price" to product.price,
                    "amount" to product.amount
                )
            ).await()
    }

    suspend fun deleteProduct(
        farmId: String,
        productId: String
    ) {
        farmsCollection
            .document(farmId)
            .collection("products")
            .document(productId)
            .delete()
            .await()
    }

    suspend fun addProductToFarm(
        farmId: String,
        product: Product
    ) {
        farmsCollection
            .document(farmId)
            .collection("products")
            .add(
                mapOf(
                    "name" to product.name,
                    "description" to product.description,
                    "price" to product.price,
                    "amount" to product.amount
                )
            ).await()
    }
}
