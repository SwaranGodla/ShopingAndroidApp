# DemoApp - Modern Android E-Commerce Shopping App

A comprehensive native Android e-commerce shopping application built with modern Android development practices. This app showcases Clean Architecture principles, Jetpack Compose UI, and demonstrates a complete shopping experience with authentication, product browsing, cart management, and order processing.

## 🚀 Key Features

### Authentication & User Management
- **Secure Login/Registration**: Email-based authentication with password recovery
- **User Profile Management**: Edit personal information, manage addresses
- **Session Management**: Persistent login with secure token handling

### Product Catalog & Discovery
- **Product Browsing**: Browse products with pagination and infinite scroll
- **Category Navigation**: Filter products by categories (Electronics, Clothing, Books, Home, Sports, Beauty, Toys)
- **Product Search**: Search products by name, description, or brand
- **Product Details**: Comprehensive product information with images, reviews, and specifications
- **Related Products**: Smart product recommendations
- **Product Reviews**: Read and write product reviews with ratings

### Shopping Cart & Checkout
- **Smart Cart Management**: Add/remove items, update quantities, save for later
- **Real-time Cart Updates**: Live cart item count with badge notifications
- **Checkout Process**: Multi-step checkout with address selection
- **Payment Integration**: Secure payment processing with multiple payment methods
- **Order Confirmation**: Detailed order confirmation with tracking information

### Order Management
- **Order History**: Complete order tracking and history
- **Order Status Tracking**: Real-time order status updates (Pending, Processing, Shipped, Delivered, Cancelled)
- **Order Details**: Detailed order information with item breakdown
- **Address Management**: Multiple shipping addresses with default selection

### User Experience
- **Wishlist/Favorites**: Save products for later purchase
- **Modern UI/UX**: Material 3 design with smooth animations
- **Responsive Design**: Optimized for different screen sizes
- **Dark/Light Theme**: Adaptive theming support
- **Offline Support**: Local data caching with Room database

## 🏗️ Architecture Overview

This application follows **Clean Architecture** principles with clear separation of concerns:

### Presentation Layer
- **Jetpack Compose**: Modern declarative UI toolkit
- **MVVM Pattern**: ViewModels handle UI state and business logic
- **Navigation**: Type-safe navigation with Compose Navigation
- **State Management**: Reactive state management with Kotlin Flows
- **Custom Components**: Reusable UI components with animations

### Domain Layer
- **Use Cases**: Business logic encapsulation
- **Repository Interfaces**: Abstract data access contracts
- **Domain Models**: Core business entities
- **Resource Wrapper**: Generic result handling (Success, Error, Loading)

### Data Layer
- **Repository Implementations**: Concrete data access logic
- **Local Database**: Room database for offline data storage
- **Remote API**: Retrofit for network communication
- **Mock Data**: Comprehensive fake data for development/testing

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin 1.9.22**: Modern, concise programming language
- **Android SDK 31+**: Latest Android development platform
- **Jetpack Compose**: Declarative UI toolkit
- **Material 3**: Latest Material Design system

### Architecture & Dependency Injection
- **Hilt 2.50**: Dependency injection framework
- **Clean Architecture**: Layered architecture pattern
- **MVVM**: Model-View-ViewModel pattern

### Data Management
- **Room 2.6.1**: Local SQLite database
- **Retrofit 2.9.0**: HTTP client for API communication
- **Kotlin Coroutines 1.7.1**: Asynchronous programming
- **Kotlin Flows**: Reactive streams

### UI & Animation
- **Compose Animation**: Smooth UI animations
- **Coil 2.5.0**: Image loading and caching
- **Navigation Compose**: Type-safe navigation

### Development & Testing
- **Timber 5.0.1**: Logging framework
- **MockK 1.13.9**: Mocking framework for testing
- **JUnit 4.13.2**: Unit testing framework
- **Espresso**: UI testing framework

## 📱 App Screens & Navigation

### Authentication Flow
- **Splash Screen**: App initialization and authentication check
- **Login Screen**: Email/password authentication with validation
- **Registration Screen**: New user account creation
- **Forgot Password Screen**: Password recovery functionality

### Main Application Flow
- **Home Screen**: Featured products, categories, and search
- **Categories Screen**: Browse products by category
- **Product Detail Screen**: Comprehensive product information with reviews
- **Cart Screen**: Shopping cart management and checkout initiation
- **Profile Screen**: User account management and settings

### Profile Management
- **Edit Profile**: Update personal information
- **Order History**: Complete order tracking
- **Order Detail**: Individual order information
- **Address Management**: Shipping address management
- **Wishlist**: Saved favorite products

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or newer
- **Kotlin**: 1.9.22 or newer
- **Android SDK**: API Level 31+ (Android 12+)
- **JDK**: 11 or newer
- **Gradle**: 8.11.0

### Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/DemoAppWindsurf.git
   cd DemoAppWindsurf
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Sync the project**:
   - Android Studio will automatically sync Gradle files
   - Wait for dependency downloads to complete

4. **Run the application**:
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

## 🔧 Development Mode & Mock Data

The app includes a comprehensive mock data implementation for development and testing purposes. This allows you to run the app without needing an actual backend server.

### Mock Data Features

The app is configured to use mock data by default (`USE_MOCK_REPOSITORIES = true` in `ShoppingApplication.kt`). This provides:

- **Realistic Data**: Generated fake data that mimics real e-commerce scenarios
- **Network Simulation**: Simulated network delays for realistic user experience
- **Complete Functionality**: All features work with mock data including authentication, cart, orders, and payments
- **No Backend Required**: Run the app immediately without server setup

### Mock Repository Implementations

The following mock repositories provide comprehensive fake data:

- **`MockAuthRepositoryImpl`**: User authentication with fake tokens and user data
- **`MockProductRepositoryImpl`**: Product catalog with categories, reviews, and search functionality
- **`MockCartRepositoryImpl`**: Shopping cart management with persistent local storage
- **`MockOrderRepositoryImpl`**: Order processing with status tracking and history
- **`MockPaymentRepositoryImpl`**: Payment processing simulation with multiple payment methods

### Test Credentials & Data

**Authentication**: Use any email and password combination:
- Email: `user@example.com` (or any valid email format)
- Password: `password` (or any password)

**Sample Data Includes**:
- 10+ product categories (Electronics, Clothing, Books, Home, Sports, Beauty, Toys)
- 50+ products with realistic details, images, and reviews
- Multiple payment methods (Visa, Mastercard)
- Sample orders with different statuses
- User profiles and addresses

### Switching Between Mock and Real API

To switch between mock data and real API implementations:

1. **Open** `app/src/main/java/com/example/myapplication/ShoppingApplication.kt`
2. **Modify** the `USE_MOCK_REPOSITORIES` constant:
   ```kotlin
   // For mock data (default)
   private const val USE_MOCK_REPOSITORIES = true
   
   // For real API
   private const val USE_MOCK_REPOSITORIES = false
   ```
3. **Rebuild** the project to apply changes

### Build Variants

The app supports multiple build variants:

- **Debug**: Development build with debug features enabled
- **Staging**: Pre-production testing environment
- **Release**: Production-ready build with optimizations

## 📊 Project Structure

```
app/
├── src/main/java/com/example/myapplication/
│   ├── data/                          # Data Layer
│   │   ├── local/                     # Local database (Room)
│   │   │   ├── dao/                   # Data Access Objects
│   │   │   ├── entity/                # Database entities
│   │   │   └── ShoppingDatabase.kt    # Database configuration
│   │   ├── model/                     # Data models
│   │   ├── remote/                    # API services
│   │   ├── repository/                # Repository implementations
│   │   └── util/                      # Data utilities (FakeDataProvider)
│   ├── domain/                        # Domain Layer
│   │   ├── repository/                # Repository interfaces
│   │   ├── usecase/                   # Business logic use cases
│   │   └── util/                      # Domain utilities (Resource)
│   ├── presentation/                 # Presentation Layer
│   │   ├── components/                # Reusable UI components
│   │   ├── navigation/                # Navigation setup
│   │   ├── screens/                   # Screen composables
│   │   └── theme/                     # App theming
│   ├── di/                           # Dependency Injection
│   └── MainActivity.kt               # Main activity
```

## 🎯 Key Features Deep Dive

### Authentication System
- **Secure Login**: Email/password authentication with validation
- **Registration**: New user account creation with profile setup
- **Password Recovery**: Forgot password functionality
- **Session Management**: Persistent login with secure token storage
- **Profile Management**: Edit personal information and preferences

### Product Catalog
- **Product Browsing**: Infinite scroll with pagination
- **Category Filtering**: Browse by product categories
- **Search Functionality**: Real-time product search
- **Product Details**: Comprehensive product information
- **Image Gallery**: Multiple product images with zoom
- **Reviews & Ratings**: User-generated reviews and ratings
- **Related Products**: Smart product recommendations

### Shopping Cart
- **Add to Cart**: Seamless product addition
- **Quantity Management**: Update item quantities
- **Cart Persistence**: Local storage with Room database
- **Real-time Updates**: Live cart count with badge notifications
- **Save for Later**: Move items to wishlist
- **Cart Validation**: Stock availability checking

### Checkout & Payment
- **Multi-step Checkout**: Address selection and payment method
- **Address Management**: Multiple shipping addresses
- **Payment Methods**: Support for multiple payment options
- **Order Confirmation**: Detailed order summary
- **Order Tracking**: Real-time order status updates

### Order Management
- **Order History**: Complete order tracking
- **Order Details**: Individual order information
- **Status Updates**: Real-time order status (Pending, Processing, Shipped, Delivered, Cancelled)
- **Order Cancellation**: Cancel orders within allowed timeframe

## 🧪 Testing

The app includes comprehensive testing setup:

### Unit Tests
- **Repository Tests**: Mock repository implementations testing
- **Use Case Tests**: Business logic validation
- **ViewModel Tests**: UI state management testing
- **Utility Tests**: Helper function validation

### UI Tests
- **Screen Tests**: Individual screen functionality
- **Navigation Tests**: Navigation flow validation
- **Integration Tests**: End-to-end user flows

### Running Tests
```bash
# Run all tests
./gradlew test

# Run unit tests only
./gradlew testDebugUnitTest

# Run UI tests
./gradlew connectedAndroidTest
```

## 🚀 Performance & Optimization

### App Performance
- **Lazy Loading**: Efficient list rendering with Compose LazyColumn
- **Image Optimization**: Coil for efficient image loading and caching
- **Database Optimization**: Room database with proper indexing
- **Memory Management**: Proper lifecycle management and memory leaks prevention

### Build Optimization
- **ProGuard**: Code obfuscation and optimization for release builds
- **Resource Shrinking**: Unused resource removal
- **Code Shrinking**: Dead code elimination
- **Multi-APK**: Different APKs for different screen densities

## 🔒 Security Features

- **Secure Authentication**: Token-based authentication with secure storage
- **Data Validation**: Input validation and sanitization
- **Network Security**: HTTPS communication with certificate pinning
- **Local Storage**: Encrypted local database storage
- **ProGuard**: Code obfuscation for release builds

## 📱 Device Compatibility

- **Minimum SDK**: API 31 (Android 12)
- **Target SDK**: API 36 (Latest Android)
- **Screen Support**: Phone and tablet layouts
- **Orientation**: Portrait and landscape support
- **Accessibility**: Material Design accessibility guidelines

## 🤝 Contributing

We welcome contributions to improve this e-commerce app! Here's how you can contribute:

### Development Setup
1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** following the existing code style
4. **Add tests** for new functionality
5. **Commit your changes**: `git commit -m 'Add some amazing feature'`
6. **Push to the branch**: `git push origin feature/amazing-feature`
7. **Open a Pull Request**

### Code Style Guidelines
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add proper documentation for public APIs
- Write unit tests for new features
- Follow Clean Architecture principles

### Reporting Issues
- Use GitHub Issues to report bugs
- Provide detailed reproduction steps
- Include device and OS information
- Add screenshots for UI issues

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgements

### Technologies & Libraries
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI toolkit
- [Material Design 3](https://m3.material.io/) - Latest Material Design system
- [Hilt](https://dagger.dev/hilt/) - Dependency injection framework
- [Room](https://developer.android.com/training/data-storage/room) - Local database
- [Retrofit](https://square.github.io/retrofit/) - HTTP client
- [Coil](https://coil-kt.github.io/coil/) - Image loading library
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) - Type-safe navigation

### Design Inspiration
- [Material Design Guidelines](https://material.io/design)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## 📞 Support

For support, questions, or feedback:
- Create an issue on GitHub
- Check the documentation
- Review the code examples

---

**Built with ❤️ using modern Android development practices**
