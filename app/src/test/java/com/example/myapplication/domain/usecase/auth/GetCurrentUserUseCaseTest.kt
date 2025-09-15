package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.UserDto
import com.example.myapplication.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCurrentUserUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    }

    @Test
    fun `getCurrentUser returns user when logged in`() = runTest {
        // Given
        val user = UserDto(
            id = "user1",
            name = "Test User",
            email = "test@example.com",
            phoneNumber = "1234567890",
            profileImage = null,
            createdAt = System.currentTimeMillis().toString()
        )
        every { authRepository.getCurrentUser() } returns flowOf(user)

        // When
        val result = getCurrentUserUseCase().first()

        // Then
        assertEquals(user, result)
    }

    @Test
    fun `getCurrentUser returns null when not logged in`() = runTest {
        // Given
        every { authRepository.getCurrentUser() } returns flowOf(null)

        // When
        val result = getCurrentUserUseCase().first()

        // Then
        assertNull(result)
    }
}
