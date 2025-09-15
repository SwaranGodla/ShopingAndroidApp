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
class LoginUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `login with empty email returns error`() = runTest {
        // When
        val result = loginUseCase("", "password123")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `login with empty password returns error`() = runTest {
        // When
        val result = loginUseCase("test@example.com", "")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Password cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `login with invalid email format returns error`() = runTest {
        // When
        val result = loginUseCase("invalid-email", "password123")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Invalid email format", (result as Resource.Error).message)
    }

    @Test
    fun `login with valid credentials returns success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val user = UserDto(
            id = "user1",
            name = "Test User",
            email = email,
            phoneNumber = "1234567890",
            profileImage = null,
            createdAt = "2022-01-01T00:00:00.000Z"
        )
        val authResponse = AuthResponse(
            token = "test-token",
            user = user,
            message = "Login successful",
            error = null,
            success = true
        )
        coEvery { authRepository.login(email, password) } returns Resource.Success(authResponse)

        // When
        val result = loginUseCase(email, password)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(authResponse, (result as Resource.Success).data)
    }

    @Test
    fun `login with valid credentials but server error returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val errorMessage = "Authentication failed"
        coEvery { authRepository.login(email, password) } returns Resource.Error(errorMessage)

        // When
        val result = loginUseCase(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
