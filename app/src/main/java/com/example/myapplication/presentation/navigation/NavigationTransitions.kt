package com.example.myapplication.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

/**
 * Helper object for navigation transitions.
 */
object NavigationTransitions {
    
    private const val TRANSITION_DURATION = 300
    
    /**
     * Slide in from the right and fade in enter transition.
     */
    fun enterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = EaseIn
            )
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
    }
    
    /**
     * Slide out to the left and fade out exit transition.
     */
    fun exitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = EaseOut
            )
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
    }
    
    /**
     * Slide in from the left and fade in pop enter transition.
     */
    fun popEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = EaseIn
            )
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
    }
    
    /**
     * Slide out to the right and fade out pop exit transition.
     */
    fun popExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = EaseOut
            )
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
    }
    
    /**
     * Slide in from the bottom and fade in enter transition.
     */
    fun enterTransitionVertical(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = EaseIn
            )
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
    }
    
    /**
     * Slide out to the bottom and fade out exit transition.
     */
    fun exitTransitionVertical(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = EaseOut
            )
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
    }
    
    /**
     * Fade in enter transition.
     */
    fun fadeEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = LinearEasing))
    }
    
    /**
     * Fade out exit transition.
     */
    fun fadeExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION, easing = LinearEasing))
    }
}
