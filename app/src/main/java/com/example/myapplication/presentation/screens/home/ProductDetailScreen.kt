package com.example.myapplication.presentation.screens.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductReview
import com.example.myapplication.presentation.components.FadeInWithDelay
import com.example.myapplication.presentation.components.PriceTag
import com.example.myapplication.presentation.components.RatingBar
import com.example.myapplication.presentation.components.ScaleBounceIn
import com.example.myapplication.presentation.components.ShimmerProductCard
import com.example.myapplication.presentation.components.StaggeredAnimatedVisibility
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/**
 * Composable for the product detail screen.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit,
    viewModel: ProductDetailViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    // Handle product detail events
    LaunchedEffect(key1 = true) {
        viewModel.productDetailEvent.collectLatest { event ->
            when (event) {
                is ProductDetailViewModel.ProductDetailEvent.AddedToCart -> {
                    snackbarHostState.showSnackbar("Product added to cart")
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (viewModel.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (viewModel.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (viewModel.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCart,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Go to Cart"
                )
            }
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
                        .verticalScroll(rememberScrollState())
                ) {
                    // Image carousel shimmer
                    ShimmerProductCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Title shimmer
                    ShimmerProductCard(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(28.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Price shimmer
                    ShimmerProductCard(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Rating shimmer
                    ShimmerProductCard(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .height(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Description title shimmer
                    ShimmerProductCard(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Description content shimmer
                    repeat(3) {
                        ShimmerProductCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Related products title shimmer
                    ShimmerProductCard(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Related products shimmer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(2) {
                            ShimmerProductCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(180.dp)
                            )
                        }
                    }
                }
            }
            
            viewModel.product?.let { product ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Image carousel
                    val pagerState = rememberPagerState(pageCount = { product.images.size })
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(product.images[page])
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "${product.name} image ${page + 1}",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        
                        // Image indicators
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(product.images.size) { index ->
                                val isSelected = pagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(if (isSelected) 10.dp else 8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }
                    }
                    
                    // Thumbnail images
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(product.images) { index, imageUrl ->
                            val isSelected = pagerState.currentPage == index
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "${product.name} thumbnail ${index + 1}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                            )
                        }
                    }
                    
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Product name and brand
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "By ${product.brand}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Rating with animation
                        FadeInWithDelay(delayMillis = 300) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Use RatingBar component
                                RatingBar(
                                    rating = product.rating,
                                    starSize = 20,
                                    starColor = MaterialTheme.colorScheme.secondary
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                // Show review count (using a fixed number since reviewCount is not in Product class)
                                Text(
                                    text = "(${(product.rating * 10).toInt()} reviews)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Quantity selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Quantity:",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            IconButton(
                                onClick = { viewModel.decreaseQuantity() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease Quantity"
                                )
                            }
                            
                            Text(
                                text = viewModel.selectedQuantity.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            
                            IconButton(
                                onClick = { viewModel.increaseQuantity() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase Quantity"
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Add to cart button
                        Button(
                            onClick = { viewModel.addToCart() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = !viewModel.isLoading
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add to Cart")
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Product description
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Product details
                        Text(
                            text = "Details",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        DetailItem("Brand", product.brand)
                        DetailItem("Category", product.category)
                        DetailItem("Stock", "${product.stock} units")
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Reviews
                        Text(
                            text = "Reviews",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (viewModel.reviews.isEmpty()) {
                            Text(
                                text = "No reviews yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            viewModel.reviews.forEach { review ->
                                ReviewItem(review = review)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Related products with animation
                        if (viewModel.relatedProducts.isNotEmpty()) {
                            FadeInWithDelay(delayMillis = 500) {
                                Text(
                                    text = "You Might Also Like",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(viewModel.relatedProducts) { index, relatedProduct ->
                                    // Add staggered animation with delay based on index
                                    StaggeredAnimatedVisibility(
                                        visible = true,
                                        initialDelay = 600 + (100 * index),
                                        enter = fadeIn(animationSpec = tween(220)) + 
                                                slideInVertically(
                                                    initialOffsetY = { 40 },
                                                    animationSpec = spring(
                                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                                        stiffness = Spring.StiffnessLow
                                                    )
                                                )
                                    ) {
                                        RelatedProductItem(
                                            product = relatedProduct,
                                            onProductClick = { /* Navigate to product detail */ }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } ?: run {
                // Show error if product is null
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Product not found",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = navigateBack
                    ) {
                        Text("Go Back")
                    }
                }
            }
        }
    }
}

/**
 * Composable for displaying a product detail item.
 */
@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
    
    Divider(modifier = Modifier.padding(vertical = 4.dp))
}

/**
 * Composable for displaying a product review.
 */
@Composable
fun ReviewItem(review: ProductReview) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row {
                    repeat(5) { index ->
                        val starColor = if (index < review.rating.toInt()) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        }
                        
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = starColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            Text(
                text = review.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Composable for displaying a related product item.
 */
@Composable
fun RelatedProductItem(
    product: Product,
    onProductClick: (String) -> Unit
) {
    // Use ScaleBounceIn for a subtle animation
    ScaleBounceIn {
        Card(
            modifier = Modifier
                .width(160.dp)
                .clickable { onProductClick(product.id) },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.thumbnail)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                    
                    // Display discount badge if applicable
                    if (product.discountPercentage != null && product.discountPercentage > 0) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(bottomStart = 8.dp),
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Text(
                                text = "-${product.discountPercentage.toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Use RatingBar component
                    RatingBar(
                        rating = product.rating,
                        starSize = 12
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Use PriceTag component
                    PriceTag(
                        price = product.finalPrice,
                        originalPrice = if (product.discountPercentage != null && product.discountPercentage > 0) product.price else null
                    )
                }
            }
        }
    }
}
