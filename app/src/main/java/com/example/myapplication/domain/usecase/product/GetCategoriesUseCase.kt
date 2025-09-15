package com.example.myapplication.domain.usecase.product

import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for retrieving all product categories.
 */
class GetCategoriesUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    /**
     * Get a list of all product categories.
     *
     * @return Resource containing a list of category names or an error.
     */
    suspend operator fun invoke(): Resource<List<String>> {
        return productRepository.getCategories()
    }
}
