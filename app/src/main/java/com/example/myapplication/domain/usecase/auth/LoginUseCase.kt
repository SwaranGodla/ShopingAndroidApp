package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for user login functionality.
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Authenticate a user with email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     * @return Resource containing authentication response or an error.
     */
    suspend operator fun invoke(email: String, password: String): Resource<AuthResponse> {
        if (email.isBlank()) {
            return Resource.Error("Email cannot be empty")
        }
        
        if (password.isBlank()) {
            return Resource.Error("Password cannot be empty")
        }
        
        if (!isValidEmail(email)) {
            return Resource.Error("Invalid email format")
        }
        
        return authRepository.login(email, password)
    }
    
    /**
     * Validate email format.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailRegex.toRegex())
    }
}
