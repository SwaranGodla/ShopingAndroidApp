package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving favorite products.
 */
class GetFavoriteProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    /**
     * Get a flow of favorite products.
     *
     * @return Flow of favorite products list.
     */
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getFavoriteProducts()
    }
}
