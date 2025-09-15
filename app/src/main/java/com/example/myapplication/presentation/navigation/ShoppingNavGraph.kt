package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.presentation.screens.auth.ForgotPasswordScreen
import com.example.myapplication.presentation.screens.auth.LoginScreen
import com.example.myapplication.presentation.screens.auth.RegisterScreen
import com.example.myapplication.presentation.screens.cart.CartScreen
import com.example.myapplication.presentation.screens.cart.CheckoutScreen
import com.example.myapplication.presentation.screens.cart.OrderConfirmationScreen
import com.example.myapplication.presentation.screens.home.CategoriesScreen
import com.example.myapplication.presentation.screens.home.CategoryProductsScreen
import com.example.myapplication.presentation.screens.home.HomeScreen
import com.example.myapplication.presentation.screens.home.ProductDetailScreen
import com.example.myapplication.presentation.screens.profile.AddEditAddressScreen
import com.example.myapplication.presentation.screens.profile.AddressesScreen
import com.example.myapplication.presentation.screens.profile.EditProfileScreen
import com.example.myapplication.presentation.screens.profile.OrderDetailScreen
import com.example.myapplication.presentation.screens.profile.OrderHistoryScreen
import com.example.myapplication.presentation.screens.profile.ProfileScreen
import com.example.myapplication.presentation.screens.profile.WishListScreen
import com.example.myapplication.presentation.screens.splash.SplashScreen

/**
 * Navigation graph for the shopping application.
 * Defines all the routes and their corresponding screens.
 */
@Composable
fun ShoppingNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    val actions = remember(navController) { NavigationActions(navController) }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash screen
        composable(
            route = Screen.Splash.route,
            exitTransition = NavigationTransitions.fadeExitTransition(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition()
        ) {
            SplashScreen(
                navigateToLogin = actions.navigateToLogin,
                navigateToHome = actions.navigateToHome,
                viewModel = hiltViewModel()
            )
        }
        
        // Auth screens
        composable(
            route = Screen.Login.route,
            enterTransition = NavigationTransitions.fadeEnterTransition(),
            exitTransition = NavigationTransitions.fadeExitTransition(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition(),
            popExitTransition = NavigationTransitions.fadeExitTransition()
        ) {
            LoginScreen(
                navigateToRegister = actions.navigateToRegister,
                navigateToForgotPassword = actions.navigateToForgotPassword,
                navigateToHome = actions.navigateToHome,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.Register.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            RegisterScreen(
                navigateToLogin = actions.navigateToLogin,
                navigateToHome = actions.navigateToHome,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.ForgotPassword.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            ForgotPasswordScreen(
                navigateBack = actions.navigateBack,
                viewModel = hiltViewModel()
            )
        }
        
        // Main screens
        composable(
            route = Screen.Home.route,
            enterTransition = NavigationTransitions.fadeEnterTransition(),
            exitTransition = NavigationTransitions.fadeExitTransition(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition(),
            popExitTransition = NavigationTransitions.fadeExitTransition()
        ) {
            HomeScreen(
                navigateToProductDetail = actions.navigateToProductDetail,
                navigateToCategory = actions.navigateToCategory,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.Categories.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            CategoriesScreen(
                navigateToCategory = actions.navigateToCategory,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.Cart.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            CartScreen(
                navigateToProductDetail = actions.navigateToProductDetail,
                navigateToCheckout = actions.navigateToCheckout,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.Profile.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            ProfileScreen(
                navigateToEditProfile = actions.navigateToEditProfile,
                navigateToOrderHistory = actions.navigateToOrderHistory,
                navigateToAddresses = actions.navigateToAddresses,
                navigateToWishList = actions.navigateToWishList,
                navigateToLogin = actions.navigateToLogin,
                viewModel = hiltViewModel()
            )
        }
        
        // Detail screens
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType }),
            enterTransition = NavigationTransitions.enterTransitionVertical(),
            exitTransition = NavigationTransitions.exitTransitionVertical(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition(),
            popExitTransition = NavigationTransitions.fadeExitTransition()
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                navigateBack = actions.navigateBack,
                navigateToCart = actions.navigateToCart,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.CategoryProducts.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType }),
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryProductsScreen(
                categoryName = categoryName,
                navigateBack = actions.navigateBack,
                navigateToProductDetail = actions.navigateToProductDetail,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.Checkout.route,
            enterTransition = NavigationTransitions.enterTransitionVertical(),
            exitTransition = NavigationTransitions.exitTransitionVertical(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition(),
            popExitTransition = NavigationTransitions.fadeExitTransition()
        ) {
            CheckoutScreen(
                navigateBack = actions.navigateBack,
                navigateToOrderConfirmation = actions.navigateToOrderConfirmation,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.OrderConfirmation.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
            enterTransition = NavigationTransitions.fadeEnterTransition(),
            exitTransition = NavigationTransitions.fadeExitTransition(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition(),
            popExitTransition = NavigationTransitions.fadeExitTransition()
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderConfirmationScreen(
                orderId = orderId,
                navigateToHome = actions.navigateToHome,
                navigateToOrderDetail = actions.navigateToOrderDetail,
                viewModel = hiltViewModel()
            )
        }
        
        // Profile related screens
        composable(
            route = Screen.EditProfile.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            EditProfileScreen(
                navigateBack = actions.navigateBack,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.OrderHistory.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            OrderHistoryScreen(
                navigateBack = actions.navigateBack,
                navigateToOrderDetail = actions.navigateToOrderDetail,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
            enterTransition = NavigationTransitions.enterTransitionVertical(),
            exitTransition = NavigationTransitions.exitTransitionVertical(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition(),
            popExitTransition = NavigationTransitions.fadeExitTransition()
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(
                orderId = orderId,
                navigateBack = actions.navigateBack,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.Addresses.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            AddressesScreen(
                navigateBack = actions.navigateBack,
                navigateToAddEditAddress = actions.navigateToAddEditAddress,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.AddEditAddress.route,
            arguments = listOf(navArgument("addressId") { type = NavType.StringType }),
            enterTransition = NavigationTransitions.enterTransitionVertical(),
            exitTransition = NavigationTransitions.exitTransitionVertical(),
            popEnterTransition = NavigationTransitions.fadeEnterTransition(),
            popExitTransition = NavigationTransitions.fadeExitTransition()
        ) { backStackEntry ->
            val addressId = backStackEntry.arguments?.getString("addressId")
            AddEditAddressScreen(
                addressId = if (addressId == "new") null else addressId,
                navigateBack = actions.navigateBack,
                viewModel = hiltViewModel()
            )
        }
        
        composable(
            route = Screen.WishList.route,
            enterTransition = NavigationTransitions.enterTransition(),
            exitTransition = NavigationTransitions.exitTransition(),
            popEnterTransition = NavigationTransitions.popEnterTransition(),
            popExitTransition = NavigationTransitions.popExitTransition()
        ) {
            WishListScreen(
                navigateBack = actions.navigateBack,
                navigateToProductDetail = actions.navigateToProductDetail,
                viewModel = hiltViewModel()
            )
        }
    }
}
