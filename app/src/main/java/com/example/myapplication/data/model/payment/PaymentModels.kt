package com.example.myapplication.data.model.payment

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a payment request.
 */
data class PaymentRequest(
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("currency")
    val currency: String = "USD",
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("metadata")
    val metadata: Map<String, String>? = null
)

/**
 * Data class representing a payment intent.
 */
data class PaymentIntent(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("client_secret")
    val clientSecret: String,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("currency")
    val currency: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("created_at")
    val createdAt: String
)

/**
 * Data class representing a payment method.
 */
data class PaymentMethod(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("image_url")
    val imageUrl: String?,
    
    @SerializedName("supported_currencies")
    val supportedCurrencies: List<String>
)

/**
 * Data class representing a payment response.
 */
data class PaymentResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("currency")
    val currency: String,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("receipt_url")
    val receiptUrl: String?
)

/**
 * Enum representing payment status.
 */
enum class PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    CANCELED
}
