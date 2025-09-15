package com.example.myapplication.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.auth.LoginUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the login screen.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    
    // UI state
    var email by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    // Events
    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent: SharedFlow<LoginEvent> = _loginEvent
    
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
     * Attempt to login with the current email and password.
     */
    fun login() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = loginUseCase(email, password)) {
                is Resource.Success -> {
                    if (result.data.success) {
                        _loginEvent.emit(LoginEvent.Success)
                    } else {
                        errorMessage = result.data.error ?: "Login failed"
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
     * Events emitted by the LoginViewModel.
     */
    sealed class LoginEvent {
        object Success : LoginEvent()
    }
}
