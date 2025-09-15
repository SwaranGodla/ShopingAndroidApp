package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductDetailsResponse
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
class GetProductDetailsUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var getProductDetailsUseCase: GetProductDetailsUseCase

    @Before
    fun setUp() {
        productRepository = mockk()
        getProductDetailsUseCase = GetProductDetailsUseCase(productRepository)
    }

    @Test
    fun `getProductDetails with empty productId returns error`() = runTest {
        // When
        val result = getProductDetailsUseCase("")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Product ID cannot be empty", (result as Resource.Error).message)
    }

    @Test
    fun `getProductDetails with valid productId returns success`() = runTest {
        // Given
        val productId = "123"
        val product = Product(
            id = productId,
            name = "Test Product",
            description = "Test description",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5f,
            stock = 50,
            brand = "Test Brand",
            category = "Test Category",
            thumbnail = "https://example.com/thumbnail.jpg",
            images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")
        )
        
        val relatedProducts = listOf(
            Product(
                id = "456",
                name = "Related Product",
                description = "Related product description",
                price = 79.99,
                discountPercentage = 5.0,
                rating = 4.0f,
                stock = 30,
                brand = "Test Brand",
                category = "Test Category",
                thumbnail = "https://example.com/related-thumbnail.jpg",
                images = listOf("https://example.com/related-image.jpg")
            )
        )
        
        val productDetailsResponse = ProductDetailsResponse(
            product = product,
            relatedProducts = relatedProducts,
            reviews = emptyList()
        )
        
        coEvery { 
            productRepository.getProductDetails(productId, false) 
        } returns Resource.Success(productDetailsResponse)

        // When
        val result = getProductDetailsUseCase(productId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(productDetailsResponse, (result as Resource.Success).data)
    }

    @Test
    fun `getProductDetails with forceRefresh true forces repository refresh`() = runTest {
        // Given
        val productId = "123"
        val product = Product(
            id = productId,
            name = "Test Product",
            description = "Test description",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5f,
            stock = 50,
            brand = "Test Brand",
            category = "Test Category",
            thumbnail = "https://example.com/thumbnail.jpg",
            images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")
        )
        
        val productDetailsResponse = ProductDetailsResponse(
            product = product,
            relatedProducts = emptyList(),
            reviews = emptyList()
        )
        
        coEvery { 
            productRepository.getProductDetails(productId, true) 
        } returns Resource.Success(productDetailsResponse)

        // When
        val result = getProductDetailsUseCase(productId, true)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(productDetailsResponse, (result as Resource.Success).data)
    }

    @Test
    fun `getProductDetails returns error when repository fails`() = runTest {
        // Given
        val productId = "123"
        val errorMessage = "Network error"
        
        coEvery { 
            productRepository.getProductDetails(productId, false) 
        } returns Resource.Error(errorMessage)

        // When
        val result = getProductDetailsUseCase(productId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
