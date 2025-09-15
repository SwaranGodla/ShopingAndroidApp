package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for password recovery functionality.
 */
class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Initiate password recovery process.
     *
     * @param email User's email address.
     * @return Resource containing response or an error.
     */
    suspend operator fun invoke(email: String): Resource<AuthResponse> {
        if (email.isBlank()) {
            return Resource.Error("Email cannot be empty")
        }
        
        if (!isValidEmail(email)) {
            return Resource.Error("Invalid email format")
        }
        
        return authRepository.forgotPassword(email)
    }
    
    /**
     * Validate email format.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailRegex.toRegex())
    }
}
