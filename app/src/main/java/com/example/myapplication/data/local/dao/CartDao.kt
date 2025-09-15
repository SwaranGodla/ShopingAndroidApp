package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.local.entity.CartItemEntity
import com.example.myapplication.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the cart_items table.
 */
@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity): Long

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Transaction
    @Query(
        """
        SELECT p.* FROM products p
        INNER JOIN cart_items c ON p.id = c.productId
        ORDER BY c.addedAt DESC
    """
    )
    fun getCartWithProducts(): Flow<List<ProductEntity>>

    @Query(
        """
        SELECT c.*, p.* FROM cart_items c
        INNER JOIN products p ON c.productId = p.id
        WHERE c.productId = :productId
    """
    )
    fun getCartItemWithProduct(productId: String): Flow<Map<CartItemEntity, ProductEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: String): CartItemEntity?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateCartItemQuantity(productId: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeCartItem(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT SUM(p.price * (1 - COALESCE(p.discountPercentage, 0) / 100) * c.quantity) FROM cart_items c INNER JOIN products p ON c.productId = p.id")
    fun getTotalCartPrice(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemCount(): Flow<Int>
}
