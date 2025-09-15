package com.example.myapplication.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
 * Composable for the category products screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    categoryName: String,
    navigateBack: () -> Unit,
    navigateToProductDetail: (String) -> Unit,
    viewModel: CategoryProductsViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }
    
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
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Sort button
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Popularity") },
                                onClick = {
                                    viewModel.updateSortOption(CategoryProductsViewModel.SortOption.POPULARITY)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Price: Low to High") },
                                onClick = {
                                    viewModel.updateSortOption(CategoryProductsViewModel.SortOption.PRICE_LOW_TO_HIGH)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Price: High to Low") },
                                onClick = {
                                    viewModel.updateSortOption(CategoryProductsViewModel.SortOption.PRICE_HIGH_TO_LOW)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Rating") },
                                onClick = {
                                    viewModel.updateSortOption(CategoryProductsViewModel.SortOption.RATING)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                    
                    // Filter button
                    Box {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Products") },
                                onClick = {
                                    viewModel.updateFilterOption(null)
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Under $50") },
                                onClick = {
                                    viewModel.updateFilterOption(
                                        CategoryProductsViewModel.FilterOption.PriceRange(0.0, 50.0)
                                    )
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("$50 - $100") },
                                onClick = {
                                    viewModel.updateFilterOption(
                                        CategoryProductsViewModel.FilterOption.PriceRange(50.0, 100.0)
                                    )
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("$100 - $500") },
                                onClick = {
                                    viewModel.updateFilterOption(
                                        CategoryProductsViewModel.FilterOption.PriceRange(100.0, 500.0)
                                    )
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Over $500") },
                                onClick = {
                                    viewModel.updateFilterOption(
                                        CategoryProductsViewModel.FilterOption.PriceRange(500.0, Double.MAX_VALUE)
                                    )
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("4+ Stars") },
                                onClick = {
                                    viewModel.updateFilterOption(
                                        CategoryProductsViewModel.FilterOption.Rating(4.0f)
                                    )
                                    showFilterMenu = false
                                }
                            )
                        }
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
            // Show loading indicator when loading
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            } else if (viewModel.products.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No products found",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Try changing the filters or check back later",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Active filter chip (if any)
                Column {
                    viewModel.filterOption?.let { filter ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            FilterChip(
                                selected = true,
                                onClick = { viewModel.updateFilterOption(null) },
                                label = {
                                    Text(
                                        when (filter) {
                                            is CategoryProductsViewModel.FilterOption.PriceRange -> {
                                                if (filter.max == Double.MAX_VALUE) {
                                                    "Over $${filter.min.toInt()}"
                                                } else {
                                                    "$${filter.min.toInt()} - $${filter.max.toInt()}"
                                                }
                                            }
                                            is CategoryProductsViewModel.FilterOption.Rating -> {
                                                "${filter.minRating}+ Stars"
                                            }
                                        }
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove filter"
                                    )
                                }
                            )
                        }
                    }
                    
                    // Products grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(viewModel.products) { product ->
                            CategoryProductCard(
                                product = product,
                                onClick = { navigateToProductDetail(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable for a product card in the category products screen.
 */
@Composable
fun CategoryProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Product image
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
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            
            // Product details
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Price
                val formatter = NumberFormat.getCurrencyInstance(Locale.US)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.discountPercentage != null && product.discountPercentage > 0) {
                        Text(
                            text = formatter.format(product.finalPrice),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = formatter.format(product.price),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                    } else {
                        Text(
                            text = formatter.format(product.price),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
