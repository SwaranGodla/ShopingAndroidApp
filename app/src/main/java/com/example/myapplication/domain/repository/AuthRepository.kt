package com.example.myapplication.domain.repository

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.data.model.UserDto
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication-related operations.
 */
interface AuthRepository {
    
    /**
     * Authenticate a user with email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     * @return Resource containing authentication response or an error.
     */
    suspend fun login(email: String, password: String): Resource<AuthResponse>
    
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
    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phoneNumber: String? = null
    ): Resource<AuthResponse>
    
    /**
     * Initiate password recovery process.
     *
     * @param email User's email address.
     * @return Resource containing response or an error.
     */
    suspend fun forgotPassword(email: String): Resource<AuthResponse>
    
    /**
     * Log out the current user.
     *
     * @return Resource indicating success or failure.
     */
    suspend fun logout(): Resource<Unit>
    
    /**
     * Get the current logged-in user.
     *
     * @return Flow of user data or null if no user is logged in.
     */
    fun getCurrentUser(): Flow<UserDto?>
    
    /**
     * Check if a user is currently logged in.
     *
     * @return True if a user is logged in, false otherwise.
     */
    suspend fun isUserLoggedIn(): Boolean
}
