package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the add/edit address screen.
 */
@HiltViewModel
class AddEditAddressViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Extract address ID from navigation arguments (null for new address)
    private val addressId: String? = savedStateHandle["addressId"]
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var isEditMode by mutableStateOf(false)
        private set
    
    // Form fields
    var name by mutableStateOf("")
        private set
    
    var addressLine1 by mutableStateOf("")
        private set
    
    var addressLine2 by mutableStateOf("")
        private set
    
    var city by mutableStateOf("")
        private set
    
    var state by mutableStateOf("")
        private set
    
    var zipCode by mutableStateOf("")
        private set
    
    var country by mutableStateOf("United States")
        private set
    
    var phoneNumber by mutableStateOf("")
        private set
    
    var isDefault by mutableStateOf(false)
        private set
    
    // Events
    private val _addressEvent = MutableSharedFlow<AddressEvent>()
    val addressEvent: SharedFlow<AddressEvent> = _addressEvent
    
    init {
        // If addressId is not null, we're in edit mode
        if (addressId != null && addressId != "new") {
            isEditMode = true
            loadAddress(addressId)
        }
    }
    
    /**
     * Load the address details for editing.
     */
    private fun loadAddress(id: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                // In a real app, we would call a use case to get the address
                // For this example, we'll generate mock data
                delay(1000) // Simulate network delay
                
                // Mock address data
                val mockAddress = if (id == "addr_1") {
                    Address(
                        id = "addr_1",
                        name = "John Doe",
                        addressLine1 = "123 Main Street, Apt 4B",
                        addressLine2 = null,
                        city = "San Francisco",
                        state = "CA",
                        zipCode = "94105",
                        country = "United States",
                        isDefault = true,
                        phoneNumber = "415-555-1234"
                    )
                } else {
                    Address(
                        id = "addr_2",
                        name = "John Doe",
                        addressLine1 = "456 Market Street",
                        addressLine2 = "Suite 200",
                        city = "San Francisco",
                        state = "CA",
                        zipCode = "94102",
                        country = "United States",
                        isDefault = false,
                        phoneNumber = "415-555-5678"
                    )
                }
                
                // Populate form fields
                name = mockAddress.name
                addressLine1 = mockAddress.addressLine1
                addressLine2 = mockAddress.addressLine2 ?: ""
                city = mockAddress.city
                state = mockAddress.state
                zipCode = mockAddress.zipCode
                country = mockAddress.country
                phoneNumber = mockAddress.phoneNumber ?: ""
                isDefault = mockAddress.isDefault
                
            } catch (e: Exception) {
                errorMessage = "Failed to load address: ${e.message}"
            }
            
            isLoading = false
        }
    }
    
    /**
     * Update name input field.
     */
    fun updateName(value: String) {
        name = value
    }
    
    /**
     * Update address line 1 input field.
     */
    fun updateAddressLine1(value: String) {
        addressLine1 = value
    }
    
    /**
     * Update address line 2 input field.
     */
    fun updateAddressLine2(value: String) {
        addressLine2 = value
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
     * Update country input field.
     */
    fun updateCountry(value: String) {
        country = value
    }
    
    /**
     * Update phone number input field.
     */
    fun updatePhoneNumber(value: String) {
        phoneNumber = value
    }
    
    /**
     * Update default address checkbox.
     */
    fun updateIsDefault(value: Boolean) {
        isDefault = value
    }
    
    /**
     * Save the address.
     */
    fun saveAddress() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                // Validate input fields
                if (!validateAddressForm()) {
                    isLoading = false
                    return@launch
                }
                
                // In a real app, we would call a use case to save the address
                // For this example, we'll just emit a success event
                
                // Simulate API call delay
                delay(1000)
                
                _addressEvent.emit(AddressEvent.Success)
                
            } catch (e: Exception) {
                errorMessage = "Failed to save address: ${e.message}"
            }
            
            isLoading = false
        }
    }
    
    /**
     * Validate all address form fields.
     */
    private fun validateAddressForm(): Boolean {
        if (name.isBlank()) {
            errorMessage = "Please enter your full name"
            return false
        }
        
        if (addressLine1.isBlank()) {
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
        
        if (country.isBlank()) {
            errorMessage = "Please enter your country"
            return false
        }
        
        return true
    }
    
    /**
     * Clear any error messages.
     */
    fun clearError() {
        errorMessage = null
    }
    
    /**
     * Events emitted by the AddEditAddressViewModel.
     */
    sealed class AddressEvent {
        object Success : AddressEvent()
    }
}
