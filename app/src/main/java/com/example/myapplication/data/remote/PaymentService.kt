package com.example.myapplication.data.remote

import com.example.myapplication.data.model.payment.PaymentIntent
import com.example.myapplication.data.model.payment.PaymentMethod
import com.example.myapplication.data.model.payment.PaymentRequest
import com.example.myapplication.data.model.payment.PaymentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit interface for payment-related API endpoints.
 */
interface PaymentService {
    
    /**
     * Create a payment intent.
     * 
     * @param request The payment request containing amount and currency.
     * @return A payment intent object with client secret.
     */
    @POST("payments/create-intent")
    suspend fun createPaymentIntent(@Body request: PaymentRequest): PaymentIntent
    
    /**
     * Get available payment methods.
     * 
     * @return A list of available payment methods.
     */
    @GET("payments/methods")
    suspend fun getPaymentMethods(): List<PaymentMethod>
    
    /**
     * Confirm a payment.
     * 
     * @param paymentIntentId The ID of the payment intent to confirm.
     * @return A payment response with status.
     */
    @POST("payments/confirm/{paymentIntentId}")
    suspend fun confirmPayment(@Path("paymentIntentId") paymentIntentId: String): PaymentResponse
}
