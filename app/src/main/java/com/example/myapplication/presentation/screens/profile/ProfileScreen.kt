package com.example.myapplication.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.presentation.components.FadeInWithDelay
import com.example.myapplication.presentation.components.ScaleBounceIn
import com.example.myapplication.presentation.components.ShimmerProfileInfo
import com.example.myapplication.presentation.components.StaggeredAnimatedVisibility
import kotlinx.coroutines.flow.collectLatest

/**
 * Composable for the profile screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateToEditProfile: () -> Unit,
    navigateToOrderHistory: () -> Unit,
    navigateToAddresses: () -> Unit,
    navigateToWishList: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: ProfileViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Handle profile events
    LaunchedEffect(key1 = true) {
        viewModel.profileEvent.collectLatest { event ->
            when (event) {
                is ProfileViewModel.ProfileEvent.Logout -> {
                    navigateToLogin()
                }
            }
        }
    }

    // Show error messages as snackbars
    LaunchedEffect(key1 = viewModel.errorMessage) {
        viewModel.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Show shimmer loading effects when loading
            if (viewModel.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Profile info shimmer
                    ShimmerProfileInfo()

                    Spacer(modifier = Modifier.height(32.dp))

                    // Account settings title shimmer
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(24.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Menu items shimmer
                    repeat(4) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(20.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }

                        Divider()
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // User profile header with animations
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Profile image
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                viewModel.user?.profileImage?.let { imageUrl ->
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(imageUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Profile Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    /*modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                        .align(Alignment.CenterHorizontally)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(viewModel.user?.profileImage ?: R.drawable.default_profile)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Profile Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }*/

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // User name with animation
                                    FadeInWithDelay(delayMillis = 200) {
                                        Text(
                                            text = viewModel.user?.name ?: "Guest User",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // User email with animation
                                    FadeInWithDelay(delayMillis = 300) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Email,
                                                contentDescription = "Email",
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.size(18.dp)
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = viewModel.user?.email ?: "guest@example.com",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // User phone with animation
                                    FadeInWithDelay(delayMillis = 400) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = "Phone",
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.size(18.dp)
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = viewModel.user?.phoneNumber
                                                    ?: "Not provided",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    // Edit profile button with animation
                                    FadeInWithDelay(delayMillis = 500) {
                                        Button(
                                            onClick = navigateToEditProfile,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Profile"
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = "Edit Profile",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Account settings
                        Text(
                            text = "Account Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Edit profile option
                        ProfileMenuItem(
                            icon = Icons.Default.Person,
                            title = "Edit Profile",
                            onClick = navigateToEditProfile
                        )

                        // Addresses option
                        ProfileMenuItem(
                            icon = Icons.Default.LocationOn,
                            title = "My Addresses",
                            onClick = navigateToAddresses
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Orders and wishlist
                        Text(
                            text = "My Orders & Wishlist",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Order history option
                        ProfileMenuItem(
                            icon = Icons.Default.History,
                            title = "Order History",
                            onClick = navigateToOrderHistory
                        )

                        // Wishlist option
                        ProfileMenuItem(
                            icon = Icons.Default.FavoriteBorder,
                            title = "My Wishlist",
                            onClick = navigateToWishList
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Logout option
                        ProfileMenuItem(
                            icon = Icons.Default.ExitToApp,
                            title = "Logout",
                            onClick = { showLogoutDialog = true },
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // App version
                        Text(
                            text = "App Version 1.0.0",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My Profile") }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Show shimmer loading effects when loading
                if (viewModel.isLoading) {
                    // Shimmer loading implementation
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // User profile header with animations
                        ScaleBounceIn {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                // Card content
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Account settings
                        Text(
                            text = "Account Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Edit profile option
                        ProfileMenuItem(
                            icon = Icons.Default.Person,
                            title = "Edit Profile",
                            onClick = navigateToEditProfile,
                            index = 0
                        )

                        // Addresses option
                        ProfileMenuItem(
                            icon = Icons.Default.LocationOn,
                            title = "My Addresses",
                            onClick = navigateToAddresses,
                            index = 1
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Orders and wishlist
                        Text(
                            text = "My Orders & Wishlist",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Order history option
                        ProfileMenuItem(
                            icon = Icons.Default.History,
                            title = "Order History",
                            onClick = navigateToOrderHistory,
                            index = 2
                        )

                        // Wishlist option
                        ProfileMenuItem(
                            icon = Icons.Default.FavoriteBorder,
                            title = "My Wishlist",
                            onClick = navigateToWishList,
                            index = 3
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Logout option
                        ProfileMenuItem(
                            icon = Icons.Default.ExitToApp,
                            title = "Logout",
                            onClick = { showLogoutDialog = true },
                            tint = MaterialTheme.colorScheme.error,
                            index = 4
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // App version
                        Text(
                            text = "App Version 1.0.0",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

}

/**
 * Composable for a profile menu item with animation.
 */
@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    index: Int = 0
) {
    // Use StaggeredAnimatedVisibility for a staggered entrance animation
    StaggeredAnimatedVisibility(
        visible = true,
        initialDelay = 600 + (100 * index)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() }
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with subtle scale animation
                ScaleBounceIn {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Text with fade in animation
                FadeInWithDelay(delayMillis = 100) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = tint,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Chevron icon
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider()
        }
    }
}
