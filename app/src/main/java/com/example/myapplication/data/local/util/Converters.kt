package com.example.myapplication.data.local.util

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Type converters for Room database to handle complex data types.
 */
class Converters {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    /**
     * Convert a list of strings to a JSON string for storage in the database.
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.let { json.encodeToString(it) } ?: "[]"
    }
    
    /**
     * Convert a JSON string from the database to a list of strings.
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            try {
                json.decodeFromString(value)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
