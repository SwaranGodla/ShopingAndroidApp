package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.AuthResponse
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
class ForgotPasswordUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var forgotPasswordUseCase: ForgotPasswordUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        forgotPasswordUseCase = ForgotPasswordUseCase(authRepository)
    }

    @Test
    fun `forgotPassword with empty email returns error`() = runTest {
        // When
        val result = forgotPasswordUseCase("")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `forgotPassword with invalid email format returns error`() = runTest {
        // When
        val result = forgotPasswordUseCase("invalid-email")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Invalid email format", (result as Resource.Error).message)
    }

    @Test
    fun `forgotPassword with valid email returns success`() = runTest {
        // Given
        val email = "test@example.com"
        val successResponse = AuthResponse(
            token = null,
            user = null,
            message = "Password reset email sent",
            success =  true,
            error = null
        )
        coEvery { authRepository.forgotPassword(email) } returns Resource.Success(successResponse)

        // When
        val result = forgotPasswordUseCase(email)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(successResponse, (result as Resource.Success).data)
    }

    @Test
    fun `forgotPassword with valid email but server error returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val errorMessage = "User not found"
        coEvery { authRepository.forgotPassword(email) } returns Resource.Error(errorMessage)

        // When
        val result = forgotPasswordUseCase(email)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
