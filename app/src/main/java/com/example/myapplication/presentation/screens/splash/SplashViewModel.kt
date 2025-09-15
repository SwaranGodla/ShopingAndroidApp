package com.example.myapplication.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the splash screen.
 * Handles authentication check and navigation decisions.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    init {
        checkAuthStatus()
    }

    /**
     * Check if the user is logged in and update the state accordingly.
     * Adds a small delay to show the splash screen animation.
     */
    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                // Add a small delay to show the splash screen animation
                delay(1500)
                
                // Check if user is logged in
                val isLoggedIn = authRepository.isUserLoggedIn()
                _isUserLoggedIn.value = isLoggedIn
            } catch (e: Exception) {
                // If there's an error, assume user is not logged in
                _isUserLoggedIn.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}
