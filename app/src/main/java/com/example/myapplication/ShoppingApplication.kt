package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.DefaultRepositoryModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application class for the Shopping app.
 * The @HiltAndroidApp annotation triggers Hilt's code generation.
 */
@HiltAndroidApp
class ShoppingApplication : Application() {
    
    companion object {
        // Set this to false to use real repositories with actual API calls
        // Set to true to use mock repositories with fake data
        private const val USE_MOCK_REPOSITORIES = true
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize repository selection
        DefaultRepositoryModule.setUseMockRepositories(USE_MOCK_REPOSITORIES)
        
        // Initialize Timber for logging
        /*if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }*/
    }
}
