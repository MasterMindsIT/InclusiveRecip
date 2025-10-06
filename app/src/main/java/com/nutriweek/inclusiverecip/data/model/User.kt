package com.nutriweek.inclusiverecip.data.model



data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val hashedPassword: String
)

