package com.example.myapplication.presentation.navigation

import androidx.navigation.NavController

/**
 * Helper class for navigation actions in the shopping application.
 * Provides methods for navigating between different screens.
 */
class NavigationActions(private val navController: NavController) {
    
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
    
    // Auth navigation
    val navigateToLogin: () -> Unit = {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
    
    val navigateToRegister: () -> Unit = {
        navController.navigate(Screen.Register.route)
    }
    
    val navigateToForgotPassword: () -> Unit = {
        navController.navigate(Screen.ForgotPassword.route)
    }
    
    // Main navigation
    val navigateToHome: () -> Unit = {
        navController.navigate(Screen.Home.route) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }
    
    val navigateToCategories: () -> Unit = {
        navController.navigate(Screen.Categories.route)
    }
    
    val navigateToCart: () -> Unit = {
        navController.navigate(Screen.Cart.route)
    }
    
    val navigateToProfile: () -> Unit = {
        navController.navigate(Screen.Profile.route)
    }
    
    // Detail navigation
    val navigateToProductDetail: (String) -> Unit = { productId ->
        navController.navigate(Screen.ProductDetail.createRoute(productId))
    }
    
    val navigateToCategory: (String) -> Unit = { categoryName ->
        navController.navigate(Screen.CategoryProducts.createRoute(categoryName))
    }
    
    val navigateToCheckout: () -> Unit = {
        navController.navigate(Screen.Checkout.route)
    }
    
    val navigateToOrderConfirmation: (String) -> Unit = { orderId ->
        navController.navigate(Screen.OrderConfirmation.createRoute(orderId)) {
            popUpTo(Screen.Cart.route) { inclusive = true }
        }
    }
    
    // Profile related navigation
    val navigateToEditProfile: () -> Unit = {
        navController.navigate(Screen.EditProfile.route)
    }
    
    val navigateToOrderHistory: () -> Unit = {
        navController.navigate(Screen.OrderHistory.route)
    }
    
    val navigateToOrderDetail: (String) -> Unit = { orderId ->
        navController.navigate(Screen.OrderDetail.createRoute(orderId))
    }
    
    val navigateToAddresses: () -> Unit = {
        navController.navigate(Screen.Addresses.route)
    }
    
    val navigateToAddEditAddress: (String?) -> Unit = { addressId ->
        navController.navigate(Screen.AddEditAddress.createRoute(addressId))
    }
    
    val navigateToWishList: () -> Unit = {
        navController.navigate(Screen.WishList.route)
    }
}
