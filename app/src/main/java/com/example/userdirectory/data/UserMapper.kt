package com.example.userdirectory.data

import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.network.UserNetworkDto

// Converts Network DTO to Database Entity
fun UserNetworkDto.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        username = this.username,
        email = this.email,
        phone = this.phone,
        website = this.website
    )
}