package com.example.myapplication.domain.usecase.product

import com.example.myapplication.domain.repository.ProductRepository
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
class ToggleFavoriteProductUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var toggleFavoriteProductUseCase: ToggleFavoriteProductUseCase

    @Before
    fun setUp() {
        productRepository = mockk()
        toggleFavoriteProductUseCase = ToggleFavoriteProductUseCase(productRepository)
    }

    @Test
    fun `toggleFavorite with empty productId returns error`() = runTest {
        // When
        val result = toggleFavoriteProductUseCase("", true)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Product ID cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `toggleFavorite to true returns success`() = runTest {
        // Given
        val productId = "123"
        val isFavorite = true
        coEvery { 
            productRepository.updateFavoriteStatus(productId, isFavorite) 
        } returns Resource.Success(Unit)

        // When
        val result = toggleFavoriteProductUseCase(productId, isFavorite)

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `toggleFavorite to false returns success`() = runTest {
        // Given
        val productId = "123"
        val isFavorite = false
        coEvery { 
            productRepository.updateFavoriteStatus(productId, isFavorite) 
        } returns Resource.Success(Unit)

        // When
        val result = toggleFavoriteProductUseCase(productId, isFavorite)

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `toggleFavorite returns error when repository fails`() = runTest {
        // Given
        val productId = "123"
        val isFavorite = true
        val errorMessage = "Failed to update favorite status"
        coEvery { 
            productRepository.updateFavoriteStatus(productId, isFavorite) 
        } returns Resource.Error(errorMessage)

        // When
        val result = toggleFavoriteProductUseCase(productId, isFavorite)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
