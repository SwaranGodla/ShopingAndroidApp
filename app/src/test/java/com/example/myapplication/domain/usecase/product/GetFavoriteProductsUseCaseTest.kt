package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoriteProductsUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var getFavoriteProductsUseCase: GetFavoriteProductsUseCase

    @Before
    fun setUp() {
        productRepository = mockk()
        getFavoriteProductsUseCase = GetFavoriteProductsUseCase(productRepository)
    }

    @Test
    fun `getFavoriteProducts returns list of favorite products`() = runTest {
        // Given
        val favoriteProducts = listOf(
            Product(
                id = "1",
                name = "Favorite Product 1",
                description = "Favorite product description 1",
                price = 99.99,
                discountPercentage = 10.0,
                rating = 4.5f,
                stock = 50,
                brand = "Favorite Brand",
                category = "Favorite Category",
                thumbnail = "https://example.com/favorite-thumbnail1.jpg",
                images = listOf("https://example.com/favorite-image1.jpg")
            ),
            Product(
                id = "2",
                name = "Favorite Product 2",
                description = "Favorite product description 2",
                price = 149.99,
                discountPercentage = 15.0,
                rating = 4.8f,
                stock = 30,
                brand = "Favorite Brand",
                category = "Favorite Category",
                thumbnail = "https://example.com/favorite-thumbnail2.jpg",
                images = listOf("https://example.com/favorite-image2.jpg")
            )
        )
        
        every { productRepository.getFavoriteProducts() } returns flowOf(favoriteProducts)

        // When
        val result = getFavoriteProductsUseCase().first()

        // Then
        assertEquals(2, result.size)
        assertEquals(favoriteProducts, result)
    }

    @Test
    fun `getFavoriteProducts returns empty list when no favorites`() = runTest {
        // Given
        val emptyList = emptyList<Product>()
        every { productRepository.getFavoriteProducts() } returns flowOf(emptyList)

        // When
        val result = getFavoriteProductsUseCase().first()

        // Then
        assertTrue(result.isEmpty())
    }
}
