package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.UserDto
import com.example.myapplication.domain.usecase.auth.GetCurrentUserUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the profile screen.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var user by mutableStateOf<UserDto?>(null)
        private set
    
    // Events
    private val _profileEvent = MutableSharedFlow<ProfileEvent>()
    val profileEvent: SharedFlow<ProfileEvent> = _profileEvent
    
    init {
        loadUserProfile()
    }
    
    /**
     * Load the current user's profile.
     */
    fun loadUserProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            getCurrentUserUseCase().collectLatest { currentUser ->
                user = currentUser
                isLoading = false
            }
        }
    }
    
    /**
     * Log out the current user.
     */
    fun logout() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            // In a real app, we would call a logout use case here
            // For this example, we'll just emit a logout event
            _profileEvent.emit(ProfileEvent.Logout)
            
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
     * Events emitted by the ProfileViewModel.
     */
    sealed class ProfileEvent {
        object Logout : ProfileEvent()
    }
}
