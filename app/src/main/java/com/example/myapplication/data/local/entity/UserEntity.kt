package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.data.model.UserDto

/**
 * Entity class representing a user in the local database.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val profileImage: String?,
    val createdAt: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * Extension function to convert a UserDto to a UserEntity.
 */
fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        profileImage = profileImage,
        createdAt = createdAt
    )
}

/**
 * Extension function to convert a UserEntity to a UserDto.
 */
fun UserEntity.toUserDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        profileImage = profileImage,
        createdAt = createdAt
    )
}
