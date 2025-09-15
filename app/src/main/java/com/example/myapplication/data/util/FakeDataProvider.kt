package com.example.myapplication.data.util

import com.example.myapplication.data.model.Address
import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderItem
import com.example.myapplication.data.model.OrderStatus
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductCategory
import com.example.myapplication.data.model.ProductDetailsResponse
import com.example.myapplication.data.model.ProductReview
import com.example.myapplication.data.model.UserDto
import com.example.myapplication.data.model.payment.PaymentIntent
import com.example.myapplication.data.model.payment.PaymentMethod
import com.example.myapplication.data.model.payment.PaymentResponse
import java.util.Date
import java.util.UUID
import kotlin.random.Random

/**
 * Utility class for generating fake data for testing and development.
 */
object FakeDataProvider {

    /**
     * Generate a fake authentication response.
     */
    fun generateAuthResponse(email: String): AuthResponse {
        return AuthResponse(
            success = true,
            message = "Login successful",
            token = "fake_token_${UUID.randomUUID()}",
            user = UserDto(
                id = UUID.randomUUID().toString(),
                name = "Test User",
                email = email,
                phoneNumber = "1234567890",
                profileImage = "https://i.pravatar.cc/300",
                createdAt = Date().toString()
            ),
            error = null
        )
    }

    /**
     * Generate a list of fake products.
     */
    fun generateProducts(count: Int = 10): List<Product> {
        val categories = listOf("Electronics", "Clothing", "Books", "Home", "Sports")
        val brands = listOf("Apple", "Samsung", "Nike", "Adidas", "Amazon")

        return List(count) { index ->
            Product(
                id = UUID.randomUUID().toString(),
                name = "Product ${index + 1}",
                description = "This is a description for Product ${index + 1}. It's a great product with many features.",
                price = Random.nextDouble(10.0, 1000.0),
                discountPercentage = if (Random.nextBoolean()) Random.nextDouble(
                    5.0,
                    30.0
                ) else null,
                rating = Random.nextFloat() * 5,
                stock = Random.nextInt(0, 100),
                brand = brands.random(),
                category = categories.random(),
                thumbnail = "https://picsum.photos/seed/${index}/300/300",
                images = List(3) { "https://picsum.photos/seed/${index + it}/300/300" }
            )
        }
    }

    /**
     * Generate a fake product details response.
     */
    fun generateProductDetails(productId: String): ProductDetailsResponse {
        val product = generateProducts(1).first().copy(id = productId)
        val relatedProducts = generateProducts(5)
        val reviews = generateProductReviews(product.id)

        return ProductDetailsResponse(
            product = product,
            relatedProducts = relatedProducts,
            reviews = reviews
        )
    }

    /**
     * Generate fake product reviews.
     */
    fun generateProductReviews(productId: String, count: Int = 5): List<ProductReview> {
        return List(count) { index ->
            ProductReview(
                id = UUID.randomUUID().toString(),
                userId = UUID.randomUUID().toString(),
                userName = "User ${index + 1}",
                rating = Random.nextInt(1, 6).toFloat(),
                comment = "This is a review comment for the product. It's ${if (index % 2 == 0) "great" else "good"}!",
                date = Date().toString()
            )
        }
    }

    /**
     * Generate fake product categories.
     */
    fun generateProductCategories(): List<ProductCategory> {
        val categories =
            listOf("Electronics", "Clothing", "Books", "Home", "Sports", "Beauty", "Toys")

        return categories.mapIndexed { index, category ->
            ProductCategory(
                name = category,
                image = "https://picsum.photos/seed/${category}/300/300",
                productCount = Random.nextInt(10, 100)
            )
        }
    }

    /**
     * Generate fake orders.
     */
    fun generateOrders(count: Int = 5): List<Order> {
        val products = generateProducts(10)
        val statuses = listOf(
            OrderStatus.PENDING,
            OrderStatus.PROCESSING,
            OrderStatus.SHIPPED,
            OrderStatus.DELIVERED,
            OrderStatus.CANCELLED
        )

        return List(count) { index ->
            val orderItems = List(Random.nextInt(1, 5)) { itemIndex ->
                val product = products[Random.nextInt(products.size)]
                OrderItem(
                    id = UUID.randomUUID().toString(),
                    productId = product.id,
                    productName = product.name,
                    productImage = product.thumbnail,
                    quantity = Random.nextInt(1, 5),
                    price = product.price,
                    totalPrice = product.price * Random.nextInt(1, 5)
                )
            }

            val addredss = Address(
                street = "123 Test Street",
                city = "Test City",
                state = "Test State",
                zipCode = "12345",
                country = "Test Country",
                phone = "123-456-7890",
                name = "John Doe",
                isDefault = true,
                id = UUID.randomUUID().toString()
            )

            Order(
                id = UUID.randomUUID().toString(),
                userId = UUID.randomUUID().toString(),
                orderDate = Date(),
                items = orderItems,
                subtotal = orderItems.sumOf { it.price }.toDouble(),
                tax = 0.0,
                shippingCost = 0.0,
                totalAmount = orderItems.sumOf { it.totalPrice }.toDouble(),
                total = orderItems.sumOf { it.totalPrice }.toDouble(),
                status = statuses[index % statuses.size],
                paymentMethod = "Credit Card",
                createdAt = Date().toString(),
                shippingAddress = addredss,
                billingAddress = addredss,
                trackingNumber = "1234567890",
                estimatedDeliveryDate = Date(),
                notes = "No special instructions",
                updatedAt = Date().toString(),
            )
        }
    }

    /**
     * Generate a fake payment intent.
     */
    fun generatePaymentIntent(amount: Double): PaymentIntent {
        return PaymentIntent(
            id = UUID.randomUUID().toString(),
            clientSecret = "pi_${UUID.randomUUID()}_secret_${UUID.randomUUID()}",
            amount = amount,
            currency = "USD",
            status = "requires_payment_method",
            createdAt = Date().toString()
        )
    }


    /**
     * Generate fake payment methods.
     */
    fun generatePaymentMethods(): List<PaymentMethod> {
        return listOf(
            PaymentMethod(
                id = "pm_card_visa",
                type = "card",
                name = "visa",
                imageUrl = "https://example.com/visa.png",
                supportedCurrencies = listOf("USD", "EUR")
            ),
            PaymentMethod(
                id = "pm_card_mastercard",
                type = "card",
                name = "mastercard",
                imageUrl = "https://example.com/mastercard.png",
                supportedCurrencies = listOf("USD", "EUR")
            )
        )
    }

    /**
     * Generate a fake payment response.
     */
    fun generatePaymentResponse(paymentIntentId: String): PaymentResponse {
        return PaymentResponse(
            id = paymentIntentId,
            status = "succeeded",
            amount = 100.0,
            currency = "USD",
            createdAt = Date().toString(),
            receiptUrl = "https://example.com/receipt"
        )
    }
}
