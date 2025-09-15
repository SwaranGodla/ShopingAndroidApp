package com.example.myapplication.di.qualifiers

import javax.inject.Qualifier

/**
 * Qualifier annotation to mark real repository implementations that use actual API and database.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealRepository

/**
 * Qualifier annotation to mark mock repository implementations for testing and development.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockRepository
