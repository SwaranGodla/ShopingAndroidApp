package com.example.myapplication.data.repository

import com.example.myapplication.data.model.payment.PaymentIntent
import com.example.myapplication.data.model.payment.PaymentMethod
import com.example.myapplication.data.model.payment.PaymentRequest
import com.example.myapplication.data.model.payment.PaymentResponse
import com.example.myapplication.data.remote.PaymentService
import com.example.myapplication.domain.repository.PaymentRepository
import com.example.myapplication.domain.util.Resource
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of the PaymentRepository interface.
 */
class PaymentRepositoryImpl @Inject constructor(
    private val paymentService: PaymentService
) : PaymentRepository {
    
    override suspend fun createPaymentIntent(
        amount: Double,
        currency: String,
        description: String?
    ): Resource<PaymentIntent> {
        return try {
            val request = PaymentRequest(
                amount = amount,
                currency = currency,
                description = description
            )
            
            val paymentIntent = paymentService.createPaymentIntent(request)
            Resource.Success(paymentIntent)
        } catch (e: Exception) {
            Timber.e(e, "Error creating payment intent")
            Resource.Error("Failed to create payment intent: ${e.message}")
        }
    }
    
    override suspend fun getPaymentMethods(): Resource<List<PaymentMethod>> {
        return try {
            val paymentMethods = paymentService.getPaymentMethods()
            Resource.Success(paymentMethods)
        } catch (e: Exception) {
            Timber.e(e, "Error getting payment methods")
            Resource.Error("Failed to get payment methods: ${e.message}")
        }
    }
    
    override suspend fun confirmPayment(paymentIntentId: String): Resource<PaymentResponse> {
        return try {
            val paymentResponse = paymentService.confirmPayment(paymentIntentId)
            Resource.Success(paymentResponse)
        } catch (e: Exception) {
            Timber.e(e, "Error confirming payment")
            Resource.Error("Failed to confirm payment: ${e.message}")
        }
    }
}
