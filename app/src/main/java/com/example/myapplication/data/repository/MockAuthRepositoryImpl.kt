package com.example.myapplication.data.repository

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.data.model.UserDto
import com.example.myapplication.data.util.FakeDataProvider
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock implementation of AuthRepository that uses fake data for testing and development.
 */
@Singleton
class MockAuthRepositoryImpl @Inject constructor() : AuthRepository {
    
    private val currentUser = MutableStateFlow<UserDto?>(null)
    
    override suspend fun login(email: String, password: String): Resource<AuthResponse> {
        // Simulate network delay
        delay(1000)
        
        // Generate fake auth response
        val response = FakeDataProvider.generateAuthResponse(email)
        
        // Update current user
        currentUser.value = response.user
        
        return Resource.Success(response)
    }
    
    override suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phoneNumber: String?
    ): Resource<AuthResponse> {
        // Simulate network delay
        delay(1000)
        
        // Generate fake auth response
        val response = FakeDataProvider.generateAuthResponse(email)
        
        // Update current user
        currentUser.value = response.user
        
        return Resource.Success(response)
    }
    
    override suspend fun forgotPassword(email: String): Resource<AuthResponse> {
        // Simulate network delay
        delay(1000)
        
        // Generate fake auth response for password reset
        val response = AuthResponse(
            success = true,
            message = "Password reset email sent to $email",
            token = null,
            user = null,
            error = null
        )
        
        return Resource.Success(response)
    }
    
    override fun getCurrentUser(): Flow<UserDto?> = flow {
        // Emit current user
        emit(currentUser.value)
    }
    
    override suspend fun logout(): Resource<Unit> {
        // Simulate network delay
        delay(500)
        
        // Clear current user
        currentUser.value = null
        
        return Resource.Success(Unit)
    }
    
    suspend fun updateUserProfile(
        name: String?,
        email: String?,
        phoneNumber: String?,
        profileImage: String?
    ): Resource<UserDto> {
        // Simulate network delay
        delay(1000)
        
        // Get current user or create a new one
        val user = currentUser.value ?: FakeDataProvider.generateAuthResponse("user@example.com").user
        
        // Update user fields
        val updatedUser = user?.copy(
            name = name ?: user.name,
            email = email ?: user.email,
            phoneNumber = phoneNumber ?: user.phoneNumber,
            profileImage = profileImage ?: user.profileImage
        )
        
        // Update current user
        currentUser.value = updatedUser
        
        return if (updatedUser != null) {
            Resource.Success(updatedUser)
        } else {
            Resource.Error("User not found")
        }
    }
    
    /**
     * Check if a user is currently logged in.
     *
     * @return True if a user is logged in, false otherwise.
     */
    override suspend fun isUserLoggedIn(): Boolean {
        return currentUser.value != null
    }
}
