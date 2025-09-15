package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.UserDto
import com.example.myapplication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the current logged-in user.
 */
class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Get the current logged-in user.
     *
     * @return Flow of user data or null if no user is logged in.
     */
    operator fun invoke(): Flow<UserDto?> {
        return authRepository.getCurrentUser()
    }
}
