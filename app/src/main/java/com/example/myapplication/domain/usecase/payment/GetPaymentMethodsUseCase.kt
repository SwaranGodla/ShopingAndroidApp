package com.example.myapplication.domain.usecase.payment

import com.example.myapplication.data.model.payment.PaymentMethod
import com.example.myapplication.domain.repository.PaymentRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for retrieving available payment methods.
 */
class GetPaymentMethodsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    /**
     * Get available payment methods.
     *
     * @return Resource containing a list of PaymentMethod objects or an error.
     */
    suspend operator fun invoke(): Resource<List<PaymentMethod>> {
        return paymentRepository.getPaymentMethods()
    }
}
