package com.example.myapplication.data.remote

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductDetailsResponse
import com.example.myapplication.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service interface for the shopping application.
 * Defines all network requests for authentication, products, and cart operations.
 */
interface ShoppingApiService {
    
    // Authentication endpoints
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse
    
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse
    
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): AuthResponse
    
    // Product endpoints
    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("search") searchQuery: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String? = null
    ): List<Product>
    
    @GET("products/{productId}")
    suspend fun getProductDetails(@Path("productId") productId: String): ProductDetailsResponse
    
    @GET("products/categories")
    suspend fun getCategories(): List<String>
    
    // Cart endpoints
    @GET("cart")
    suspend fun getCart(): List<Product>
    
    @POST("cart/add/{productId}")
    suspend fun addToCart(
        @Path("productId") productId: String,
        @Query("quantity") quantity: Int = 1
    ): Boolean
    
    @POST("cart/remove/{productId}")
    suspend fun removeFromCart(@Path("productId") productId: String): Boolean
    
    @POST("cart/update/{productId}")
    suspend fun updateCartItem(
        @Path("productId") productId: String,
        @Query("quantity") quantity: Int
    ): Boolean
    
    // User profile endpoints
    @GET("user/profile")
    suspend fun getUserProfile(): Any
    
    @POST("user/profile/update")
    suspend fun updateUserProfile(@Body userProfile: Any): Boolean
    
    // Order endpoints
    @GET("orders")
    suspend fun getOrders(): List<Any>
    
    @POST("orders/create")
    suspend fun createOrder(@Body orderDetails: Any): Any
}
