package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.data.model.UserDto
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        registerUseCase = RegisterUseCase(authRepository)
    }

    @Test
    fun `register with empty name returns error`() = runTest {
        // When
        val result = registerUseCase(
            name = "",
            email = "test@example.com",
            password = "password123",
            confirmPassword = "password123"
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Name cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `register with empty email returns error`() = runTest {
        // When
        val result = registerUseCase(
            name = "Test User",
            email = "",
            password = "password123",
            confirmPassword = "password123"
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `register with invalid email format returns error`() = runTest {
        // When
        val result = registerUseCase(
            name = "Test User",
            email = "invalid-email",
            password = "password123",
            confirmPassword = "password123"
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Invalid email format", (result as Resource.Error).message)
    }

    @Test
    fun `register with empty password returns error`() = runTest {
        // When
        val result = registerUseCase(
            name = "Test User",
            email = "test@example.com",
            password = "",
            confirmPassword = ""
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Password cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `register with short password returns error`() = runTest {
        // When
        val result = registerUseCase(
            name = "Test User",
            email = "test@example.com",
            password = "12345",
            confirmPassword = "12345"
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Password must be at least 6 characters long", (result as Resource.Error).message)
    }

    @Test
    fun `register with mismatched passwords returns error`() = runTest {
        // When
        val result = registerUseCase(
            name = "Test User",
            email = "test@example.com",
            password = "password123",
            confirmPassword = "different123"
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Passwords do not match", (result as Resource.Error).message)
    }

    @Test
    fun `register with invalid phone number returns error`() = runTest {
        // When
        val result = registerUseCase(
            name = "Test User",
            email = "test@example.com",
            password = "password123",
            confirmPassword = "password123",
            phoneNumber = "abc123" // Invalid phone number
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Invalid phone number format", (result as Resource.Error).message)
    }

    @Test
    fun `register with valid inputs returns success`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"
        val confirmPassword = "password123"
        val phoneNumber = "1234567890"
        
        val user = UserDto(
            id = "user1",
            name = name,
            email = email,
            phoneNumber = phoneNumber,
            profileImage = null,
            createdAt = "2023-01-01T00:00:00.000Z"
        )
        val authResponse = AuthResponse(
            token = "test-token",
            user = user,
            message = "Registration successful",
            error = null,
            success = true
        )
        
        coEvery { 
            authRepository.register(name, email, password, confirmPassword, phoneNumber) 
        } returns Resource.Success(authResponse)

        // When
        val result = registerUseCase(name, email, password, confirmPassword, phoneNumber)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(authResponse, (result as Resource.Success).data)
    }

    @Test
    fun `register with valid inputs but server error returns error`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"
        val confirmPassword = "password123"
        val phoneNumber = "1234567890"
        val errorMessage = "Registration failed"
        
        coEvery { 
            authRepository.register(name, email, password, confirmPassword, phoneNumber) 
        } returns Resource.Error(errorMessage)

        // When
        val result = registerUseCase(name, email, password, confirmPassword, phoneNumber)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
