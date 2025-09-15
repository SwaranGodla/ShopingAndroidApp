package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.UserDto
import com.example.myapplication.domain.usecase.auth.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the edit profile screen.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var name by mutableStateOf("")
        private set
    
    var email by mutableStateOf("")
        private set
    
    var phoneNumber by mutableStateOf("")
        private set
    
    var profileImage by mutableStateOf<String?>(null)
        private set
    
    // Events
    private val _editProfileEvent = MutableSharedFlow<EditProfileEvent>()
    val editProfileEvent: SharedFlow<EditProfileEvent> = _editProfileEvent
    
    init {
        loadUserProfile()
    }
    
    /**
     * Load the current user's profile.
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            getCurrentUserUseCase().collectLatest { user ->
                user?.let {
                    name = it.name
                    email = it.email
                    phoneNumber = it.phoneNumber ?: ""
                    profileImage = it.profileImage
                }
                isLoading = false
            }
        }
    }
    
    /**
     * Update name input field.
     */
    fun updateName(newName: String) {
        name = newName
    }
    
    /**
     * Update phone number input field.
     */
    fun updatePhoneNumber(newPhoneNumber: String) {
        phoneNumber = newPhoneNumber
    }
    
    /**
     * Update profile image.
     */
    fun updateProfileImage(newProfileImage: String) {
        profileImage = newProfileImage
    }
    
    /**
     * Save profile changes.
     */
    fun saveProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            // Validate input fields
            if (name.isBlank()) {
                errorMessage = "Name cannot be empty"
                isLoading = false
                return@launch
            }
            
            // In a real app, we would call a use case to update the user profile
            // For this example, we'll just emit a success event
            
            // Simulate API call delay
            kotlinx.coroutines.delay(1000)
            
            _editProfileEvent.emit(EditProfileEvent.Success)
            
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
     * Events emitted by the EditProfileViewModel.
     */
    sealed class EditProfileEvent {
        object Success : EditProfileEvent()
    }
}
