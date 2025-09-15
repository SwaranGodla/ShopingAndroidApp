package com.example.myapplication.domain.usecase.payment

import com.example.myapplication.data.model.payment.PaymentIntent
import com.example.myapplication.domain.repository.PaymentRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for creating a payment intent.
 */
class CreatePaymentIntentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    /**
     * Create a payment intent.
     *
     * @param amount The amount to charge.
     * @param currency The currency code (default: USD).
     * @param description Optional description for the payment.
     * @return Resource containing a PaymentIntent or an error.
     */
    suspend operator fun invoke(
        amount: Double,
        currency: String = "USD",
        description: String? = null
    ): Resource<PaymentIntent> {
        // Validate amount
        if (amount <= 0) {
            return Resource.Error("Amount must be greater than zero")
        }
        
        // Validate currency
        if (currency.isBlank()) {
            return Resource.Error("Currency cannot be empty")
        }
        
        return paymentRepository.createPaymentIntent(amount, currency, description)
    }
}
