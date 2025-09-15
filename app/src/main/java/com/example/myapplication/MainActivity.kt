package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.domain.usecase.cart.GetCartStatsUseCase
import com.example.myapplication.presentation.navigation.Screen
import com.example.myapplication.presentation.navigation.ShoppingNavGraph
import com.example.myapplication.presentation.theme.ShoppingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity for the Shopping application.
 * Uses Jetpack Compose for UI and Hilt for dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var getCartStatsUseCase: GetCartStatsUseCase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAppTheme {
                ShoppingApp(getCartStatsUseCase)
            }
        }
    }
}

/**
 * Data class representing a bottom navigation item.
 */
data class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

/**
 * Main composable function for the Shopping application.
 * Sets up the navigation and bottom navigation bar.
 */
@Composable
fun ShoppingApp(getCartStatsUseCase: GetCartStatsUseCase) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Bottom navigation items
    val bottomNavItems = listOf(
        BottomNavItem(
            screen = Screen.Home,
            title = "Home",
            icon = Icons.Filled.Home
        ),
        BottomNavItem(
            screen = Screen.Categories,
            title = "Categories",
            icon = Icons.Outlined.Category
        ),
        BottomNavItem(
            screen = Screen.Cart,
            title = "Cart",
            icon = Icons.Filled.ShoppingCart
        ),
        BottomNavItem(
            screen = Screen.Profile,
            title = "Profile",
            icon = Icons.Filled.Person
        )
    )
    
    // Cart item count for badge
    val cartItemCount by getCartStatsUseCase.getCartItemCount().collectAsState(initial = 0)
    
    // Main screen with bottom navigation
    Scaffold(
        bottomBar = {
            // Only show bottom navigation on main screens
            val showBottomNav = currentDestination?.route in bottomNavItems.map { it.screen.route }
            if (showBottomNav) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { 
                            it.route == item.screen.route 
                        } == true
                        
                        NavigationBarItem(
                            icon = {
                                if (item.screen == Screen.Cart && cartItemCount > 0) {
                                    BadgedBox(badge = { Badge { Text(text = cartItemCount.toString()) } }) {
                                        Icon(imageVector = item.icon, contentDescription = item.title)
                                    }
                                } else {
                                    Icon(imageVector = item.icon, contentDescription = item.title)
                                }
                            },
                            label = { Text(text = item.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            ShoppingNavGraph(navController = navController)
        }
    }
}