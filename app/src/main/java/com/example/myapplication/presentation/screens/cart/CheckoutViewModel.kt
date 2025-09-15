package com.example.myapplication.presentation.screens.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.payment.PaymentIntent
import com.example.myapplication.data.model.payment.PaymentMethod
import com.example.myapplication.data.model.payment.PaymentResponse
import com.example.myapplication.domain.usecase.cart.ClearCartUseCase
import com.example.myapplication.domain.usecase.cart.GetCartItemsUseCase
import com.example.myapplication.domain.usecase.cart.GetCartStatsUseCase
import com.example.myapplication.domain.usecase.payment.ConfirmPaymentUseCase
import com.example.myapplication.domain.usecase.payment.CreatePaymentIntentUseCase
import com.example.myapplication.domain.usecase.payment.GetPaymentMethodsUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * ViewModel for the checkout screen.
 */
@HiltViewModel
class CheckoutViewModel @Inject constructor(
    getCartItemsUseCase: GetCartItemsUseCase,
    getCartStatsUseCase: GetCartStatsUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val confirmPaymentUseCase: ConfirmPaymentUseCase
) : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
        
    var paymentIntent by mutableStateOf<PaymentIntent?>(null)
        private set
        
    var paymentMethods by mutableStateOf<List<PaymentMethod>>(emptyList())
        private set
        
    var selectedPaymentMethodId by mutableStateOf<String?>(null)
        private set
        
    var paymentStatus by mutableStateOf<PaymentStatus>(PaymentStatus.NOT_STARTED)
        private set
    
    // Form fields
    var fullName by mutableStateOf("")
        private set
    
    var email by mutableStateOf("")
        private set
    
    var phone by mutableStateOf("")
        private set
    
    var address by mutableStateOf("")
        private set
    
    var city by mutableStateOf("")
        private set
    
    var state by mutableStateOf("")
        private set
    
    var zipCode by mutableStateOf("")
        private set
    
    var paymentMethod by mutableStateOf("credit_card")
        private set
    
    var cardNumber by mutableStateOf("")
        private set
    
    var cardExpiry by mutableStateOf("")
        private set
    
    var cardCvv by mutableStateOf("")
        private set
    
    // Cart items as a StateFlow
    val cartItems: StateFlow<List<Product>> = getCartItemsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Cart total price as a StateFlow
    val cartTotal: StateFlow<Double> = getCartStatsUseCase.getCartTotal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
    
    // Events
    private val _checkoutEvent = MutableSharedFlow<CheckoutEvent>()
    val checkoutEvent: SharedFlow<CheckoutEvent> = _checkoutEvent
    
    init {
        loadPaymentMethods()
    }
    
    /**
     * Update full name input field.
     */
    fun updateFullName(value: String) {
        fullName = value
    }
    
    /**
     * Update email input field.
     */
    fun updateEmail(value: String) {
        email = value
    }
    
    /**
     * Update phone input field.
     */
    fun updatePhone(value: String) {
        phone = value
    }
    
    /**
     * Update address input field.
     */
    fun updateAddress(value: String) {
        address = value
    }
    
    /**
     * Update city input field.
     */
    fun updateCity(value: String) {
        city = value
    }
    
    /**
     * Update state input field.
     */
    fun updateState(value: String) {
        state = value
    }
    
    /**
     * Update zip code input field.
     */
    fun updateZipCode(value: String) {
        zipCode = value
    }
    
    /**
     * Update payment method selection.
     */
    fun updatePaymentMethod(value: String) {
        paymentMethod = value
    }
    
    /**
     * Update card number input field.
     */
    fun updateCardNumber(value: String) {
        cardNumber = value
    }
    
    /**
     * Update card expiry input field.
     */
    fun updateCardExpiry(value: String) {
        cardExpiry = value
    }
    
    /**
     * Update card CVV input field.
     */
    fun updateCardCvv(value: String) {
        cardCvv = value
    }
    
    /**
     * Load available payment methods.
     */
    private fun loadPaymentMethods() {
        viewModelScope.launch {
            when (val result = getPaymentMethodsUseCase()) {
                is Resource.Success -> {
                    paymentMethods = result.data
                    if (paymentMethods.isNotEmpty()) {
                        selectedPaymentMethodId = paymentMethods.first().id
                    }
                }
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
            }
        }
    }
    
    /**
     * Update selected payment method.
     */
    fun updateSelectedPaymentMethod(paymentMethodId: String) {
        selectedPaymentMethodId = paymentMethodId
    }
    
    /**
     * Process the checkout with the current information.
     */
    fun processCheckout() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            paymentStatus = PaymentStatus.PROCESSING
            
            // Validate form fields
            if (!validateCheckoutForm()) {
                isLoading = false
                paymentStatus = PaymentStatus.NOT_STARTED
                return@launch
            }
            
            // Create payment intent
            val total = cartTotal.value + (cartTotal.value * 0.08) // Add tax
            when (val result = createPaymentIntentUseCase(
                amount = total,
                currency = "USD",
                description = "Order from Shopping App"
            )) {
                is Resource.Success -> {
                    paymentIntent = result.data
                    processPayment()
                }
                is Resource.Error -> {
                    errorMessage = result.message
                    paymentStatus = PaymentStatus.FAILED
                    isLoading = false
                }
                else -> {}
            }
        }
    }
    
    /**
     * Process the payment after creating a payment intent.
     */
    private fun processPayment() {
        viewModelScope.launch {
            paymentIntent?.let { intent ->
                // In a real app, we would collect payment details and process the payment
                // For this example, we'll just simulate a successful payment
                
                when (val result = confirmPaymentUseCase(intent.id)) {
                    is Resource.Success -> {
                        val response = result.data
                        if (response.status == "succeeded") {
                            paymentStatus = PaymentStatus.SUCCEEDED
                            
                            // Clear the cart
                            when (val clearResult = clearCartUseCase()) {
                                is Resource.Success -> {
                                    // Generate a random order ID
                                    val orderId = "ORD-${Random.nextInt(100000, 999999)}"
                                    _checkoutEvent.emit(CheckoutEvent.Success(orderId))
                                }
                                is Resource.Error -> {
                                    errorMessage = clearResult.message
                                    paymentStatus = PaymentStatus.FAILED
                                }
                                else -> {}
                            }
                        } else {
                            errorMessage = "Payment failed: ${response.status}"
                            paymentStatus = PaymentStatus.FAILED
                        }
                    }
                    is Resource.Error -> {
                        errorMessage = result.message
                        paymentStatus = PaymentStatus.FAILED
                    }
                    else -> {}
                }
            } ?: run {
                errorMessage = "Payment intent not created"
                paymentStatus = PaymentStatus.FAILED
            }
            
            isLoading = false
        }
    }
    
    /**
     * Validate all checkout form fields.
     */
    private fun validateCheckoutForm(): Boolean {
        if (fullName.isBlank()) {
            errorMessage = "Please enter your full name"
            return false
        }
        
        if (email.isBlank() || !isValidEmail(email)) {
            errorMessage = "Please enter a valid email address"
            return false
        }
        
        if (phone.isBlank()) {
            errorMessage = "Please enter your phone number"
            return false
        }
        
        if (address.isBlank()) {
            errorMessage = "Please enter your address"
            return false
        }
        
        if (city.isBlank()) {
            errorMessage = "Please enter your city"
            return false
        }
        
        if (state.isBlank()) {
            errorMessage = "Please enter your state"
            return false
        }
        
        if (zipCode.isBlank()) {
            errorMessage = "Please enter your zip code"
            return false
        }
        
        if (paymentMethod == "credit_card") {
            if (cardNumber.isBlank() || cardNumber.length < 16) {
                errorMessage = "Please enter a valid card number"
                return false
            }
            
            if (cardExpiry.isBlank() || !isValidExpiry(cardExpiry)) {
                errorMessage = "Please enter a valid expiry date (MM/YY)"
                return false
            }
            
            if (cardCvv.isBlank() || cardCvv.length < 3) {
                errorMessage = "Please enter a valid CVV"
                return false
            }
        }
        
        return true
    }
    
    /**
     * Validate email format.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailRegex.toRegex())
    }
    
    /**
     * Validate card expiry format.
     */
    private fun isValidExpiry(expiry: String): Boolean {
        val expiryRegex = "^(0[1-9]|1[0-2])/([0-9]{2})$"
        return expiry.matches(expiryRegex.toRegex())
    }
    
    /**
     * Clear any error messages.
     */
    fun clearError() {
        errorMessage = null
    }
    
    /**
     * Events emitted by the CheckoutViewModel.
     */
    sealed class CheckoutEvent {
        data class Success(val orderId: String) : CheckoutEvent()
    }
    
    /**
     * Enum representing payment status.
     */
    enum class PaymentStatus {
        NOT_STARTED,
        PROCESSING,
        SUCCEEDED,
        FAILED
    }
}
