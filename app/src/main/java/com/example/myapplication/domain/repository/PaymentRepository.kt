package com.example.myapplication.domain.repository

import com.example.myapplication.data.model.payment.PaymentIntent
import com.example.myapplication.data.model.payment.PaymentMethod
import com.example.myapplication.data.model.payment.PaymentResponse
import com.example.myapplication.domain.util.Resource

/**
 * Repository interface for payment-related operations.
 */
interface PaymentRepository {
    
    /**
     * Create a payment intent.
     * 
     * @param amount The amount to charge.
     * @param currency The currency code (default: USD).
     * @param description Optional description for the payment.
     * @return Resource containing a PaymentIntent or an error.
     */
    suspend fun createPaymentIntent(
        amount: Double,
        currency: String = "USD",
        description: String? = null
    ): Resource<PaymentIntent>
    
    /**
     * Get available payment methods.
     * 
     * @return Resource containing a list of PaymentMethod objects or an error.
     */
    suspend fun getPaymentMethods(): Resource<List<PaymentMethod>>
    
    /**
     * Confirm a payment.
     * 
     * @param paymentIntentId The ID of the payment intent to confirm.
     * @return Resource containing a PaymentResponse or an error.
     */
    suspend fun confirmPayment(paymentIntentId: String): Resource<PaymentResponse>
}
