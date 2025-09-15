package com.example.myapplication.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.auth.RegisterUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the registration screen.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    
    // UI state
    var name by mutableStateOf("")
        private set
    
    var email by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set
    
    var confirmPassword by mutableStateOf("")
        private set
    
    var phoneNumber by mutableStateOf("")
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    // Events
    private val _registerEvent = MutableSharedFlow<RegisterEvent>()
    val registerEvent: SharedFlow<RegisterEvent> = _registerEvent
    
    /**
     * Update name input field.
     */
    fun updateName(newName: String) {
        name = newName
        errorMessage = null
    }
    
    /**
     * Update email input field.
     */
    fun updateEmail(newEmail: String) {
        email = newEmail
        errorMessage = null
    }
    
    /**
     * Update password input field.
     */
    fun updatePassword(newPassword: String) {
        password = newPassword
        errorMessage = null
    }
    
    /**
     * Update confirm password input field.
     */
    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        errorMessage = null
    }
    
    /**
     * Update phone number input field.
     */
    fun updatePhoneNumber(newPhoneNumber: String) {
        phoneNumber = newPhoneNumber
        errorMessage = null
    }
    
    /**
     * Attempt to register with the current user details.
     */
    fun register() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = registerUseCase(
                name = name,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                phoneNumber = phoneNumber.takeIf { it.isNotBlank() }
            )) {
                is Resource.Success -> {
                    if (result.data.success) {
                        _registerEvent.emit(RegisterEvent.Success)
                    } else {
                        errorMessage = result.data.error ?: "Registration failed"
                    }
                }
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
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
     * Events emitted by the RegisterViewModel.
     */
    sealed class RegisterEvent {
        object Success : RegisterEvent()
    }
}
