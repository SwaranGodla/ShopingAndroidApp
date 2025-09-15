package com.example.myapplication.domain.usecase.payment

import com.example.myapplication.data.model.payment.PaymentResponse
import com.example.myapplication.domain.repository.PaymentRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for confirming a payment.
 */
class ConfirmPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    /**
     * Confirm a payment.
     *
     * @param paymentIntentId The ID of the payment intent to confirm.
     * @return Resource containing a PaymentResponse or an error.
     */
    suspend operator fun invoke(paymentIntentId: String): Resource<PaymentResponse> {
        // Validate payment intent ID
        if (paymentIntentId.isBlank()) {
            return Resource.Error("Payment intent ID cannot be empty")
        }
        
        return paymentRepository.confirmPayment(paymentIntentId)
    }
}
