package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.data.model.Product

/**
 * Entity class representing a product in the local database.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double?,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * Extension function to convert a ProductEntity to a Product domain model.
 */
fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}

/**
 * Extension function to convert a Product domain model to a ProductEntity.
 */
fun Product.toProductEntity(isFavorite: Boolean = false): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images,
        isFavorite = isFavorite
    )
}
