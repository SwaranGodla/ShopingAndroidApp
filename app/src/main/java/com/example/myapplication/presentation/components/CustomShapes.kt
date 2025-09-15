package com.example.myapplication.presentation.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * A custom shape that clips a rectangle from the end.
 */
class ClipRectEnd(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}

/**
 * A custom shape for a diagonal cut at the bottom.
 */
class DiagonalCutShape(private val cutHeight: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = androidx.compose.ui.graphics.Path().apply {
                lineTo(0f, size.height - cutHeight)
                lineTo(size.width, size.height)
                lineTo(size.width, 0f)
                close()
            }
        )
    }
}

/**
 * A custom shape for a ticket with notches.
 */
class TicketShape(private val cornerRadius: Float, private val notchRadius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = androidx.compose.ui.graphics.Path().apply {
                // Top left corner
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = cornerRadius * 2,
                        bottom = cornerRadius * 2
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = true
                )
                
                // Top edge
                lineTo(size.width - cornerRadius, 0f)
                
                // Top right corner
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadius * 2,
                        top = 0f,
                        right = size.width,
                        bottom = cornerRadius * 2
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                
                // Right edge with notch
                lineTo(size.width, size.height / 2 - notchRadius)
                arcTo(
                    rect = Rect(
                        left = size.width - notchRadius * 2,
                        top = size.height / 2 - notchRadius,
                        right = size.width,
                        bottom = size.height / 2 + notchRadius
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                lineTo(size.width, size.height - cornerRadius)
                
                // Bottom right corner
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadius * 2,
                        top = size.height - cornerRadius * 2,
                        right = size.width,
                        bottom = size.height
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                
                // Bottom edge
                lineTo(cornerRadius, size.height)
                
                // Bottom left corner
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = size.height - cornerRadius * 2,
                        right = cornerRadius * 2,
                        bottom = size.height
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                
                // Left edge with notch
                lineTo(0f, size.height / 2 + notchRadius)
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = size.height / 2 - notchRadius,
                        right = notchRadius * 2,
                        bottom = size.height / 2 + notchRadius
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                lineTo(0f, cornerRadius)
                
                close()
            }
        )
    }
}

/**
 * A custom shape for a wave at the bottom.
 */
class WaveShape(private val waveHeight: Float, private val waveCount: Int = 3) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = androidx.compose.ui.graphics.Path().apply {
                lineTo(0f, size.height - waveHeight)
                
                val waveWidth = size.width / waveCount
                
                for (i in 0 until waveCount) {
                    cubicTo(
                        x1 = waveWidth * i + waveWidth / 3,
                        y1 = size.height - waveHeight * 2,
                        x2 = waveWidth * i + waveWidth * 2 / 3,
                        y2 = size.height,
                        x3 = waveWidth * (i + 1),
                        y3 = size.height - waveHeight
                    )
                }
                
                lineTo(size.width, 0f)
                close()
            }
        )
    }
}

/**
 * A custom shape for a speech bubble.
 */
class SpeechBubbleShape(private val cornerRadius: Float, private val arrowSize: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = androidx.compose.ui.graphics.Path().apply {
                // Start from the arrow's base
                moveTo(size.width / 2 - arrowSize, size.height - cornerRadius)
                
                // Draw the arrow
                lineTo(size.width / 2, size.height)
                lineTo(size.width / 2 + arrowSize, size.height - cornerRadius)
                
                // Bottom edge to bottom right corner
                lineTo(size.width - cornerRadius, size.height - cornerRadius)
                
                // Bottom right corner
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadius * 2,
                        top = size.height - cornerRadius * 2,
                        right = size.width,
                        bottom = size.height
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                
                // Right edge
                lineTo(size.width, cornerRadius)
                
                // Top right corner
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadius * 2,
                        top = 0f,
                        right = size.width,
                        bottom = cornerRadius * 2
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                
                // Top edge
                lineTo(cornerRadius, 0f)
                
                // Top left corner
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = cornerRadius * 2,
                        bottom = cornerRadius * 2
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                
                // Left edge
                lineTo(0f, size.height - cornerRadius * 2)
                
                // Bottom left corner
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = size.height - cornerRadius * 2,
                        right = cornerRadius * 2,
                        bottom = size.height
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                
                // Bottom edge to arrow
                lineTo(size.width / 2 - arrowSize, size.height - cornerRadius)
                
                close()
            }
        )
    }
}
