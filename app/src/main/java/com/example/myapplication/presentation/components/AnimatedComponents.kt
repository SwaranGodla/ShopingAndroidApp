package com.example.myapplication.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Animated visibility with fade and scale animations.
 */
@Composable
fun FadeScaleAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)),
        exit = fadeOut(animationSpec = tween(90)) +
                scaleOut(targetScale = 0.92f, animationSpec = tween(90))
    ) {
        content()
    }
}

/**
 * Animated visibility with slide and fade animations.
 */
@Composable
fun SlideFadeAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(
            initialOffsetY = { 40 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(animationSpec = tween(220)),
        exit = slideOutVertically(
            targetOffsetY = { 40 },
            animationSpec = tween(220)
        ) + fadeOut(animationSpec = tween(220))
    ) {
        content()
    }
}

/**
 * Animated visibility with expand and fade animations.
 */
@Composable
fun ExpandFadeAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = expandVertically(
            expandFrom = androidx.compose.ui.Alignment.Top,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(animationSpec = tween(220)),
        exit = shrinkVertically(
            shrinkTowards = androidx.compose.ui.Alignment.Top,
            animationSpec = tween(220)
        ) + fadeOut(animationSpec = tween(220))
    ) {
        content()
    }
}

/**
 * Animated content that fades in with a delay.
 */
@Composable
fun FadeInWithDelay(
    delayMillis: Int = 300,
    durationMillis: Int = 300,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis)
    )
    
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }
    
    Box(modifier = modifier.alpha(alpha)) {
        content()
    }
}

/**
 * Animated content that scales in with a bounce effect.
 */
@Composable
fun ScaleBounceIn(
    delayMillis: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }
    
    Box(modifier = modifier.scale(scale)) {
        content()
    }
}

/**
 * Animated content that slides in from the bottom.
 */
@Composable
fun SlideInFromBottom(
    delayMillis: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else 100.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }
    
    Box(modifier = modifier.offset(y = offsetY)) {
        content()
    }
}

/**
 * Staggered animation for a list of items.
 */
@Composable
fun StaggeredAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    initialDelay: Int = 0,
    delayBetweenItems: Int = 50,
    enter: EnterTransition = fadeIn(animationSpec = tween(220)) + 
            scaleIn(initialScale = 0.92f, animationSpec = tween(220)),
    exit: ExitTransition = fadeOut(animationSpec = tween(90)) + 
            scaleOut(targetScale = 0.92f, animationSpec = tween(90)),
    content: @Composable () -> Unit
) {
    val visibleState = remember {
        MutableTransitionState(initialState = false).apply {
            targetState = false
        }
    }
    
    LaunchedEffect(visible) {
        delay(initialDelay.toLong())
        visibleState.targetState = visible
    }
    
    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}
