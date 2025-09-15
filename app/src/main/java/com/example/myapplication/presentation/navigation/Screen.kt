package com.example.myapplication.presentation.navigation

/**
 * Sealed class representing all the screens in the shopping application.
 * Each screen has a unique route used for navigation.
 */
sealed class Screen(val route: String) {
    // Auth screens
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object ForgotPassword : Screen("forgot_password_screen")
    
    // Main screens
    object Home : Screen("home_screen")
    object Categories : Screen("categories_screen")
    object Cart : Screen("cart_screen")
    object Profile : Screen("profile_screen")
    
    // Detail screens
    object ProductDetail : Screen("product_detail_screen/{productId}") {
        fun createRoute(productId: String) = "product_detail_screen/$productId"
    }
    object CategoryProducts : Screen("category_products_screen/{categoryName}") {
        fun createRoute(categoryName: String) = "category_products_screen/$categoryName"
    }
    object Checkout : Screen("checkout_screen")
    object OrderConfirmation : Screen("order_confirmation_screen/{orderId}") {
        fun createRoute(orderId: String) = "order_confirmation_screen/$orderId"
    }
    
    // Profile related screens
    object EditProfile : Screen("edit_profile_screen")
    object OrderHistory : Screen("order_history_screen")
    object OrderDetail : Screen("order_detail_screen/{orderId}") {
        fun createRoute(orderId: String) = "order_detail_screen/$orderId"
    }
    object Addresses : Screen("addresses_screen")
    object AddEditAddress : Screen("add_edit_address_screen/{addressId}") {
        fun createRoute(addressId: String? = null) = addressId?.let {
            "add_edit_address_screen/$it"
        } ?: "add_edit_address_screen/new"
    }
    object WishList : Screen("wish_list_screen")
}
