package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for user registration functionality.
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Register a new user.
     *
     * @param name User's full name.
     * @param email User's email address.
     * @param password User's password.
     * @param confirmPassword Password confirmation.
     * @param phoneNumber Optional phone number.
     * @return Resource containing registration response or an error.
     */
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phoneNumber: String? = null
    ): Resource<AuthResponse> {
        // Validate input fields
        if (name.isBlank()) {
            return Resource.Error("Name cannot be empty")
        }
        
        if (email.isBlank()) {
            return Resource.Error("Email cannot be empty")
        }
        
        if (!isValidEmail(email)) {
            return Resource.Error("Invalid email format")
        }
        
        if (password.isBlank()) {
            return Resource.Error("Password cannot be empty")
        }
        
        if (password.length < 6) {
            return Resource.Error("Password must be at least 6 characters long")
        }
        
        if (password != confirmPassword) {
            return Resource.Error("Passwords do not match")
        }
        
        // Phone number is optional, but if provided, validate it
        if (phoneNumber != null && phoneNumber.isNotBlank() && !isValidPhoneNumber(phoneNumber)) {
            return Resource.Error("Invalid phone number format")
        }
        
        return authRepository.register(name, email, password, confirmPassword, phoneNumber)
    }
    
    /**
     * Validate email format.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailRegex.toRegex())
    }
    
    /**
     * Validate phone number format.
     */
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Simple validation for demonstration purposes
        // In a real app, you might want to use a more sophisticated validation
        // or a library like libphonenumber
        val phoneRegex = "^[+]?[0-9]{10,15}$"
        return phoneNumber.matches(phoneRegex.toRegex())
    }
}
