package com.example.myapplication.domain.util

/**
 * A generic class that holds a value with its loading status.
 * @param <T> Type of the resource data.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val exception: Exception? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    
    /**
     * Returns the encapsulated data if this instance represents [Success] or null otherwise.
     */
    fun dataOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    /**
     * Returns true if this is a successful result.
     */
    val isSuccess: Boolean
        get() = this is Success
    
    /**
     * Returns true if this is an error result.
     */
    val isError: Boolean
        get() = this is Error
    
    /**
     * Returns true if this is a loading result.
     */
    val isLoading: Boolean
        get() = this is Loading
}
