package com.example.myapplication.data.repository
import com.example.myapplication.data.model.payment.PaymentIntent
import com.example.myapplication.data.model.payment.PaymentMethod
import com.example.myapplication.data.model.payment.PaymentResponse
import com.example.myapplication.data.util.FakeDataProvider
import com.example.myapplication.domain.repository.PaymentRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock implementation of PaymentRepository that uses fake data for testing and development.
 */
@Singleton
class MockPaymentRepositoryImpl @Inject constructor() : PaymentRepository {
    
    // In-memory cache of payment intents
    private val paymentIntents = mutableMapOf<String, PaymentIntent>()
    
    override suspend fun createPaymentIntent(
        amount: Double,
        currency: String,
        description: String?
    ): Resource<PaymentIntent> {
        // Simulate network delay
        delay(1000)
        
        // Generate fake payment intent
        val paymentIntent = FakeDataProvider.generatePaymentIntent(amount)
        
        // Store in cache
        paymentIntents[paymentIntent.id] = paymentIntent
        
        return Resource.Success(paymentIntent)
    }
    
    override suspend fun getPaymentMethods(): Resource<List<PaymentMethod>> {
        // Simulate network delay
        delay(1000)
        
        // Generate fake payment methods
        val paymentMethods = FakeDataProvider.generatePaymentMethods()
        
        return Resource.Success(paymentMethods)
    }
    
    override suspend fun confirmPayment(paymentIntentId: String): Resource<PaymentResponse> {
        // Simulate network delay
        delay(1500)
        
        // Check if payment intent exists
        val paymentIntent = paymentIntents[paymentIntentId]
        return if (paymentIntent != null) {
            // Generate fake payment response
            val paymentResponse = FakeDataProvider.generatePaymentResponse(paymentIntentId)
            Resource.Success(paymentResponse)
        } else {
            Resource.Error("Payment intent not found")
        }
    }
}
