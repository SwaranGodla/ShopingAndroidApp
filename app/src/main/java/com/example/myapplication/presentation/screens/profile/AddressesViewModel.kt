package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class representing a shipping address.
 */
data class Address(
    val id: String,
    val name: String,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val isDefault: Boolean,
    val phoneNumber: String?
)

/**
 * ViewModel for the addresses screen.
 */
@HiltViewModel
class AddressesViewModel @Inject constructor() : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var addresses by mutableStateOf<List<Address>>(emptyList())
        private set
    
    // Events
    private val _addressEvent = MutableSharedFlow<AddressEvent>()
    val addressEvent: SharedFlow<AddressEvent> = _addressEvent
    
    init {
        loadAddresses()
    }
    
    /**
     * Load the user's saved addresses.
     */
    fun loadAddresses() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                // In a real app, we would call a use case to get the addresses
                // For this example, we'll generate some mock data
                delay(1000) // Simulate network delay
                
                addresses = generateMockAddresses()
                
            } catch (e: Exception) {
                errorMessage = "Failed to load addresses: ${e.message}"
            }
            
            isLoading = false
        }
    }
    
    /**
     * Set an address as the default address.
     */
    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                // In a real app, we would call a use case to update the address
                // For this example, we'll just update the local state
                addresses = addresses.map { address ->
                    address.copy(isDefault = address.id == addressId)
                }
                
                _addressEvent.emit(AddressEvent.DefaultAddressUpdated)
                
            } catch (e: Exception) {
                errorMessage = "Failed to update default address: ${e.message}"
            }
            
            isLoading = false
        }
    }
    
    /**
     * Delete an address.
     */
    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                // In a real app, we would call a use case to delete the address
                // For this example, we'll just update the local state
                addresses = addresses.filter { it.id != addressId }
                
                _addressEvent.emit(AddressEvent.AddressDeleted)
                
            } catch (e: Exception) {
                errorMessage = "Failed to delete address: ${e.message}"
            }
            
            isLoading = false
        }
    }
    
    /**
     * Clear any error messages.
     */
    fun clearError() {
        errorMessage = null
    }
    
    /**
     * Generate mock address data for demonstration purposes.
     */
    private fun generateMockAddresses(): List<Address> {
        return listOf(
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
            ),
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
        )
    }
    
    /**
     * Events emitted by the AddressesViewModel.
     */
    sealed class AddressEvent {
        object DefaultAddressUpdated : AddressEvent()
        object AddressDeleted : AddressEvent()
    }
}
