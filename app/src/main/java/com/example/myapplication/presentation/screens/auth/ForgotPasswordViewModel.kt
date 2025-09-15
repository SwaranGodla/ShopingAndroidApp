package com.example.myapplication.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.auth.ForgotPasswordUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the forgot password screen.
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {
    
    // UI state
    var email by mutableStateOf("")
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var successMessage by mutableStateOf<String?>(null)
        private set
    
    // Events
    private val _forgotPasswordEvent = MutableSharedFlow<ForgotPasswordEvent>()
    val forgotPasswordEvent: SharedFlow<ForgotPasswordEvent> = _forgotPasswordEvent
    
    /**
     * Update email input field.
     */
    fun updateEmail(newEmail: String) {
        email = newEmail
        errorMessage = null
        successMessage = null
    }
    
    /**
     * Attempt to send password reset email.
     */
    fun resetPassword() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null
            
            when (val result = forgotPasswordUseCase(email)) {
                is Resource.Success -> {
                    if (result.data.success) {
                        successMessage = "Password reset email sent. Please check your inbox."
                        _forgotPasswordEvent.emit(ForgotPasswordEvent.Success)
                    } else {
                        errorMessage = result.data.error ?: "Failed to send reset email"
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
     * Clear any success messages.
     */
    fun clearSuccess() {
        successMessage = null
    }
    
    /**
     * Events emitted by the ForgotPasswordViewModel.
     */
    sealed class ForgotPasswordEvent {
        object Success : ForgotPasswordEvent()
    }
}
