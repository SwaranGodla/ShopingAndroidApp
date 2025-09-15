package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a login request with email and password.
 */
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

/**
 * Data class representing a registration request with user details.
 */
data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirm_password") val confirmPassword: String,
    @SerializedName("phone_number") val phoneNumber: String? = null
)

/**
 * Data class representing the authentication response from the server.
 */
data class AuthResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String? = null,
    @SerializedName("user") val user: UserDto? = null,
    @SerializedName("error") val error: String? = null
)

/**
 * Data class representing user information returned from the server.
 */
data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("profile_image") val profileImage: String? = null,
    @SerializedName("created_at") val createdAt: String
)
