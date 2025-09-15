package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a product in the shopping application.
 */
data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("discount_percentage") val discountPercentage: Double? = null,
    @SerializedName("rating") val rating: Float,
    @SerializedName("stock") val stock: Int,
    @SerializedName("brand") val brand: String,
    @SerializedName("category") val category: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("images") val images: List<String>
) {
    /**
     * Calculate the final price after applying discount.
     */
    val finalPrice: Double
        get() = if (discountPercentage != null && discountPercentage > 0) {
            price * (1 - (discountPercentage / 100))
        } else {
            price
        }
}

/**
 * Data class representing detailed product information.
 */
data class ProductDetailsResponse(
    @SerializedName("product") val product: Product,
    @SerializedName("related_products") val relatedProducts: List<Product>,
    @SerializedName("reviews") val reviews: List<ProductReview>
)

/**
 * Data class representing a product review.
 */
data class ProductReview(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("comment") val comment: String,
    @SerializedName("date") val date: String
)

/**
 * Data class representing a product category with its image.
 */
data class ProductCategory(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("product_count") val productCount: Int
)
