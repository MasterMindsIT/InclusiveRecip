package com.nutriweek.inclusiverecip.data

import com.nutriweek.inclusiverecip.data.model.User


fun User.toEntity() = UserEntity(
    id = id,
    email = email,
    displayName = displayName,
    hashedPassword = hashedPassword
)

fun UserEntity.toDomain() = User(
    id = id,
    email = email,
    displayName = displayName,
    hashedPassword = hashedPassword
)
