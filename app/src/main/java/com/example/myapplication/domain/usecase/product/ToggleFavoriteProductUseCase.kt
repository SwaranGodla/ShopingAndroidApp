package com.example.myapplication.domain.usecase.product

import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for toggling the favorite status of a product.
 */
class ToggleFavoriteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    /**
     * Toggle the favorite status of a product.
     *
     * @param productId The ID of the product.
     * @param isFavorite The new favorite status.
     * @return Resource indicating success or failure.
     */
    suspend operator fun invoke(productId: String, isFavorite: Boolean): Resource<Unit> {
        if (productId.isBlank()) {
            return Resource.Error("Product ID cannot be empty")
        }
        
        return productRepository.updateFavoriteStatus(productId, isFavorite)
    }
}
