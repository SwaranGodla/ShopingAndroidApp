package com.example.myapplication.di.qualifiers

import javax.inject.Qualifier

/**
 * Qualifier annotation to indicate that a mock implementation should be used.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Mock
