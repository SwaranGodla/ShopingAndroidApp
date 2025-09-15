package com.example.myapplication.presentation.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.presentation.components.FadeInWithDelay
import com.example.myapplication.presentation.components.ScaleBounceIn

/**
 * Splash screen composable.
 * Shows an animated logo and checks authentication status before navigating.
 */
@Composable
fun SplashScreen(
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: SplashViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    
    // Animation states
    var showLogo by remember { mutableStateOf(false) }
    var showAppName by remember { mutableStateOf(false) }
    
    // Scale animation for logo
    val scale by animateFloatAsState(
        targetValue = if (showLogo) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1000)
    )
    
    // Start animations
    LaunchedEffect(key1 = true) {
        showLogo = true
        showAppName = true
    }
    
    // Handle navigation based on auth status
    LaunchedEffect(key1 = isLoading, key2 = isUserLoggedIn) {
        if (!isLoading) {
            if (isUserLoggedIn) {
                navigateToHome()
            } else {
                navigateToLogin()
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo with scale animation
            ScaleBounceIn {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App name with fade in animation
            AnimatedVisibility(
                visible = showAppName,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = { it / 2 }
                ),
                exit = fadeOut()
            ) {
                Text(
                    text = "ShopEase",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Loading indicator
            FadeInWithDelay(delayMillis = 500) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    strokeWidth = 4.dp
                )
            }
        }
        
        // Version info at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            FadeInWithDelay(delayMillis = 800) {
                Text(
                    text = "Version 1.0.0",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }
    }
}
