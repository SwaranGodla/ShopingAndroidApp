package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entity.toUserDto
import com.example.myapplication.data.local.entity.toUserEntity
import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.RegisterRequest
import com.example.myapplication.data.model.UserDto
import com.example.myapplication.data.remote.ShoppingApiService
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of the AuthRepository interface.
 * Handles authentication operations and user data management.
 */
class AuthRepositoryImpl @Inject constructor(
    private val api: ShoppingApiService,
    private val userDao: UserDao
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Resource<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            
            if (response.success && response.user != null) {
                // Save user to local database
                userDao.insertUser(response.user.toUserEntity())
            }
            
            Resource.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Login error")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phoneNumber: String?
    ): Resource<AuthResponse> {
        return try {
            val response = api.register(
                RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    phoneNumber = phoneNumber
                )
            )
            
            if (response.success && response.user != null) {
                // Save user to local database
                userDao.insertUser(response.user.toUserEntity())
            }
            
            Resource.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Registration error")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun forgotPassword(email: String): Resource<AuthResponse> {
        return try {
            val response = api.forgotPassword(email)
            Resource.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Forgot password error")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun logout(): Resource<Unit> {
        return try {
            // Clear user data from local database
            userDao.deleteAllUsers()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Logout error")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override fun getCurrentUser(): Flow<UserDto?> {
        return userDao.getCurrentUserFlow().map { entity ->
            entity?.toUserDto()
        }
    }
    
    override suspend fun isUserLoggedIn(): Boolean {
        return userDao.getCurrentUser() != null
    }
}
