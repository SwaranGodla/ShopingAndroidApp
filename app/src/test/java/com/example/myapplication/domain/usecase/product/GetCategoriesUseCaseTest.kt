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
class GetCategoriesUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Before
    fun setUp() {
        productRepository = mockk()
        getCategoriesUseCase = GetCategoriesUseCase(productRepository)
    }

    @Test
    fun `getCategories returns success with categories list`() = runTest {
        // Given
        val categories = listOf("Electronics", "Clothing", "Books", "Home & Kitchen", "Sports")
        coEvery { productRepository.getCategories() } returns Resource.Success(categories)

        // When
        val result = getCategoriesUseCase()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(categories, (result as Resource.Success).data)
    }

    @Test
    fun `getCategories returns empty list when no categories available`() = runTest {
        // Given
        val emptyCategories = emptyList<String>()
        coEvery { productRepository.getCategories() } returns Resource.Success(emptyCategories)

        // When
        val result = getCategoriesUseCase()

        // Then
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.isEmpty())
    }

    @Test
    fun `getCategories returns error when repository fails`() = runTest {
        // Given
        val errorMessage = "Failed to fetch categories"
        coEvery { productRepository.getCategories() } returns Resource.Error(errorMessage)

        // When
        val result = getCategoriesUseCase()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
