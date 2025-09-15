package com.example.myapplication.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.myapplication.presentation.components.FadeInWithDelay
import com.example.myapplication.presentation.components.PriceTag
import com.example.myapplication.presentation.components.RatingBar
import com.example.myapplication.presentation.components.ScaleBounceIn
import com.example.myapplication.presentation.components.ShimmerProductCard
import com.example.myapplication.presentation.components.StaggeredAnimatedVisibility
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.data.model.Product
import java.text.NumberFormat
import java.util.Locale

/**
 * Composable for the home screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToProductDetail: (String) -> Unit,
    navigateToCategory: (String) -> Unit,
    viewModel: HomeViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var searchQuery by remember { mutableStateOf("") }

    val featuredProducts by viewModel.featuredProducts.collectAsState()
    val newArrivals by viewModel.newArrivals.collectAsState()
    val popularProducts by viewModel.popularProducts.collectAsState()
    val categories by viewModel.categories.collectAsState()

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
                title = {
                    ScaleBounceIn {
                        Text("ShopEase")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Content of the screen
            if (viewModel.isLoading) {
                // Show loading indicator or shimmer effect when loading
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Search field shimmer
                    ShimmerProductCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Featured products shimmer
                    Text(
                        text = "Featured Products",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(2) {
                            ShimmerProductCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(200.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Categories shimmer
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(4) {
                            ShimmerProductCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                            )
                        }
                    }
                }
            } else {
                // Main content with LazyColumn
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Search bar
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            placeholder = { Text("Search products...") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // Featured products
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            SectionHeader(
                                title = "Featured Products",
                                onSeeAllClick = { /* Navigate to all featured products */ }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (featuredProducts.isEmpty()) {
                                EmptyStateMessage(message = "No featured products available")
                            } else {
                                FeaturedProductsCarousel(
                                    products = featuredProducts,
                                    onProductClick = navigateToProductDetail
                                )
                            }
                        }
                    }

                    // Categories
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            SectionHeader(
                                title = "Categories",
                                onSeeAllClick = { /* Navigate to all categories */ }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (categories.isEmpty()) {
                                EmptyStateMessage(message = "No categories available")
                            } else {
                                CategoriesRow(
                                    categories = categories,
                                    onCategoryClick = navigateToCategory
                                )
                            }
                        }
                    }

                    // New arrivals
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            SectionHeader(
                                title = "New Arrivals",
                                onSeeAllClick = { /* Navigate to all new arrivals */ }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (newArrivals.isEmpty()) {
                                EmptyStateMessage(message = "No new arrivals available")
                            } else {
                                LazyRow(
                                    contentPadding = PaddingValues(end = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(newArrivals) { product ->
                                        ProductItem(
                                            product = product,
                                            onProductClick = navigateToProductDetail,
                                            modifier = Modifier.width(160.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Popular products
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            SectionHeader(
                                title = "Popular Products",
                                onSeeAllClick = { /* Navigate to all popular products */ }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (popularProducts.isEmpty()) {
                                EmptyStateMessage(message = "No popular products available")
                            } else {
                                LazyRow(
                                    contentPadding = PaddingValues(end = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(popularProducts) { product ->
                                        ProductItem(
                                            product = product,
                                            onProductClick = navigateToProductDetail,
                                            modifier = Modifier.width(160.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable for section headers with a "See All" button.
 */
@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Use FadeInWithDelay for a subtle entrance animation
    FadeInWithDelay(
        delayMillis = 200,
        durationMillis = 400
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Add a subtle scale animation to the "See All" text
            ScaleBounceIn(
                delayMillis = 300
            ) {
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onSeeAllClick() }
                )
            }
        }
    }
}

/**
 * Composable for featured products carousel.
 */
@Composable
fun FeaturedProductsCarousel(
    products: List<Product>,
    onProductClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(products) { index, product ->
            // Add staggered animation with delay based on index
            StaggeredAnimatedVisibility(
                visible = true,
                initialDelay = 100 * index,
                enter = fadeIn(animationSpec = tween(220)) +
                        slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
            ) {
                FeaturedProductCard(
                    product = product,
                    onProductClick = onProductClick
                )
            }
        }
    }
}

/**
 * Composable for a featured product card.
 */
@Composable
fun FeaturedProductCard(
    product: Product,
    onProductClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
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
                        .height(150.dp)
                )

                // Display discount badge if applicable
                if (product.discountPercentage != null && product.discountPercentage > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(
                            topEnd = 0.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp,
                            topStart = 12.dp
                        ),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "-${product.discountPercentage.toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Use RatingBar component
                RatingBar(
                    rating = product.rating,
                    starSize = 14
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Use PriceTag component instead of PriceDisplay
                PriceTag(
                    price = product.finalPrice,
                    originalPrice = if (product.discountPercentage != null && product.discountPercentage > 0) product.price else null
                )
            }
        }
    }
}

/**
 * Composable for categories row.
 */
@Composable
fun CategoriesRow(
    categories: List<String>,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onCategoryClick = onCategoryClick
            )
        }
    }
}

/**
 * Composable for a category item.
 */
@Composable
fun CategoryItem(
    category: String,
    onCategoryClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onCategoryClick(category) }
    ) {
        Surface(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            // In a real app, we would display a category image here
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = category.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Composable for a product item in a list.
 */
@Composable
fun ProductItem(
    product: Product,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Use ScaleBounceIn for subtle animation
    ScaleBounceIn {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onProductClick(product.id) },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.thumbnail)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    // Display discount badge if applicable
                    if (product.discountPercentage != null && product.discountPercentage > 0) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(32.dp)
                        ) {
                            Text(
                                text = "${product.discountPercentage.toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = product.brand,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Use RatingBar component
                    RatingBar(
                        rating = product.rating,
                        starSize = 12
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Use PriceTag component instead of PriceDisplay
                    PriceTag(
                        price = product.finalPrice,
                        originalPrice = if (product.discountPercentage != null && product.discountPercentage > 0) product.price else null
                    )
                }
            }
        }
    }
}

/**
 * Composable for displaying product price with discount if available.
 */
@Composable
fun PriceDisplay(product: Product) {
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (product.discountPercentage != null && product.discountPercentage > 0) {
            Text(
                text = formatter.format(product.finalPrice),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = formatter.format(product.price),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(start = 4.dp)
            )
        } else {
            Text(
                text = formatter.format(product.price),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Composable for empty state messages.
 */
@Composable
fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
