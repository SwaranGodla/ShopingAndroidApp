package com.example.myapplication.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A polished button with gradient background and shadow.
 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
    )
    
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(24.dp),
        enabled = enabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * A price tag component with discount.
 */
@Composable
fun PriceTag(
    price: Double,
    originalPrice: Double? = null,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = formatter.format(price),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        
        originalPrice?.let {
            if (it > price) {
                Text(
                    text = formatter.format(it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                )
                
                val discountPercent = ((it - price) / it * 100).toInt()
                
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "-$discountPercent%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * A rating bar component.
 */
@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    starSize: Int = 16,
    starColor: Color = MaterialTheme.colorScheme.primary,
    starBackgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxRating) {
            val starFill = when {
                i <= rating -> 1f
                i - rating < 1 -> i - rating
                else -> 0f
            }
            
            Box {
                // Background star
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = starBackgroundColor,
                    modifier = Modifier.size(starSize.dp)
                )
                
                // Filled star (clipped)
                Box(
                    modifier = Modifier
                        .width((starSize * starFill).dp)
                        .height(starSize.dp)
                        .clip(ClipRectEnd(starFill))
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = starColor,
                        modifier = Modifier.size(starSize.dp)
                    )
                }
            }
        }
        
        // Show numerical rating
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

/**
 * A circular progress indicator with percentage.
 */
@Composable
fun CircularProgressWithPercentage(
    percentage: Float,
    modifier: Modifier = Modifier,
    radius: Float = 40f,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 8f
) {
    val animatedPercentage = remember { Animatable(initialValue = 0f) }
    
    LaunchedEffect(percentage) {
        launch {
            animatedPercentage.animateTo(
                targetValue = percentage,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size((radius * 2).dp)
        ) {
            // Background circle
            drawArc(
                color = color.copy(alpha = 0.2f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            
            // Progress arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedPercentage.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        
        Text(
            text = "${(animatedPercentage.value * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * A badge component for notifications or cart items count.
 */
@Composable
fun Badge(
    count: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (count > 99) "99+" else count.toString(),
            color = MaterialTheme.colorScheme.onError,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * A card with wave background.
 */
@Composable
fun WaveCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    waveColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val path = Path()
                    val waveHeight = size.height * 0.2f
                    val waveWidth = size.width / 4
                    
                    path.moveTo(0f, size.height * 0.3f)
                    
                    for (i in 0..4) {
                        path.relativeQuadraticBezierTo(
                            waveWidth / 2, if (i % 2 == 0) waveHeight else -waveHeight,
                            waveWidth, 0f
                        )
                    }
                    
                    path.lineTo(size.width, size.height)
                    path.lineTo(0f, size.height)
                    path.close()
                    
                    drawPath(path, waveColor)
                }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                content()
            }
        }
    }
}

/**
 * A pulsating dot indicator.
 */
@Composable
fun PulsatingDot(
    color: Color = MaterialTheme.colorScheme.primary,
    size: Float = 12f
) {
    var pulsating by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (pulsating) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "pulse"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (pulsating) 0.6f else 1f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )
    
    LaunchedEffect(key1 = true) {
        while (true) {
            pulsating = !pulsating
            kotlinx.coroutines.delay(1000)
        }
    }
    
    Box(
        modifier = Modifier
            .size((size * scale).dp)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha))
    )
}

/**
 * A custom ripple effect button.
 */
@Composable
fun RippleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var clicked by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                clicked = true
                onClick()
            }
            .drawBehind {
                if (clicked) {
                    // Draw ripple effect
                    drawCircle(
                        color = Color.Blue.copy(alpha = 0.2f),
                        radius = size.width * 0.8f,
                        center = Offset(size.width / 2, size.height / 2)
                    )
                }
            }
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
    
    LaunchedEffect(clicked) {
        if (clicked) {
            kotlinx.coroutines.delay(300)
            clicked = false
        }
    }
}
