package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.Product
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
class GetProductsUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun setUp() {
        productRepository = mockk()
        getProductsUseCase = GetProductsUseCase(productRepository)
    }

    @Test
    fun `getProducts returns success with products list`() = runTest {
        // Given
        val products = listOf(
            Product(
                id = "1",
                name = "Test Product 1",
                description = "Test description 1",
                price = 99.99,
                discountPercentage = 10.0,
                rating = 4.5f,
                stock = 50,
                brand = "Test Brand",
                category = "Test Category",
                thumbnail = "https://example.com/thumbnail1.jpg",
                images = listOf("https://example.com/image1.jpg")
            ),
            Product(
                id = "2",
                name = "Test Product 2",
                description = "Test description 2",
                price = 149.99,
                discountPercentage = 5.0,
                rating = 4.0f,
                stock = 30,
                brand = "Test Brand",
                category = "Test Category",
                thumbnail = "https://example.com/thumbnail2.jpg",
                images = listOf("https://example.com/image2.jpg")
            )
        )
        
        coEvery { 
            productRepository.getProducts(
                forceRefresh = false,
                category = null,
                searchQuery = null,
                page = 1,
                limit = 20
            ) 
        } returns Resource.Success(products)

        // When
        val result = getProductsUseCase()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(products, (result as Resource.Success).data)
    }

    @Test
    fun `getProducts with category filter returns filtered products`() = runTest {
        // Given
        val category = "Electronics"
        val filteredProducts = listOf(
            Product(
                id = "1",
                name = "Test Electronic Product",
                description = "Test description",
                price = 299.99,
                discountPercentage = 15.0,
                rating = 4.8f,
                stock = 25,
                brand = "Tech Brand",
                category = category,
                thumbnail = "https://example.com/thumbnail-electronic.jpg",
                images = listOf("https://example.com/image-electronic.jpg")
            )
        )
        
        coEvery { 
            productRepository.getProducts(
                forceRefresh = false,
                category = category,
                searchQuery = null,
                page = 1,
                limit = 20
            ) 
        } returns Resource.Success(filteredProducts)

        // When
        val result = getProductsUseCase(category = category)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(filteredProducts, (result as Resource.Success).data)
    }

    @Test
    fun `getProducts with search query returns matching products`() = runTest {
        // Given
        val searchQuery = "laptop"
        val matchingProducts = listOf(
            Product(
                id = "3",
                name = "Test Laptop",
                description = "Test laptop description",
                price = 999.99,
                discountPercentage = 20.0,
                rating = 4.9f,
                stock = 10,
                brand = "Laptop Brand",
                category = "Electronics",
                thumbnail = "https://example.com/thumbnail-laptop.jpg",
                images = listOf("https://example.com/image-laptop.jpg")
            )
        )
        
        coEvery { 
            productRepository.getProducts(
                forceRefresh = false,
                category = null,
                searchQuery = searchQuery,
                page = 1,
                limit = 20
            ) 
        } returns Resource.Success(matchingProducts)

        // When
        val result = getProductsUseCase(searchQuery = searchQuery)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(matchingProducts, (result as Resource.Success).data)
    }

    @Test
    fun `getProducts with forceRefresh true forces repository refresh`() = runTest {
        // Given
        val products = listOf(
            Product(
                id = "4",
                name = "Fresh Product",
                description = "Fresh product description",
                price = 49.99,
                discountPercentage = 0.0,
                rating = 5.0f,
                stock = 100,
                brand = "Fresh Brand",
                category = "Fresh Category",
                thumbnail = "https://example.com/thumbnail-fresh.jpg",
                images = listOf("https://example.com/image-fresh.jpg")
            )
        )
        
        coEvery { 
            productRepository.getProducts(
                forceRefresh = true,
                category = null,
                searchQuery = null,
                page = 1,
                limit = 20
            ) 
        } returns Resource.Success(products)

        // When
        val result = getProductsUseCase(forceRefresh = true)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(products, (result as Resource.Success).data)
    }

    @Test
    fun `getProducts with pagination parameters passes them to repository`() = runTest {
        // Given
        val page = 2
        val limit = 10
        val paginatedProducts = listOf(
            Product(
                id = "5",
                name = "Paginated Product",
                description = "Paginated product description",
                price = 59.99,
                discountPercentage = 5.0,
                rating = 4.2f,
                stock = 45,
                brand = "Page Brand",
                category = "Page Category",
                thumbnail = "https://example.com/thumbnail-page.jpg",
                images = listOf("https://example.com/image-page.jpg")
            )
        )
        
        coEvery { 
            productRepository.getProducts(
                forceRefresh = false,
                category = null,
                searchQuery = null,
                page = page,
                limit = limit
            ) 
        } returns Resource.Success(paginatedProducts)

        // When
        val result = getProductsUseCase(page = page, limit = limit)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(paginatedProducts, (result as Resource.Success).data)
    }

    @Test
    fun `getProducts returns error when repository fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { 
            productRepository.getProducts(
                forceRefresh = false,
                category = null,
                searchQuery = null,
                page = 1,
                limit = 20
            ) 
        } returns Resource.Error(errorMessage)

        // When
        val result = getProductsUseCase()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
