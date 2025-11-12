// data/network/UserNetworkDto.kt
package com.example.userdirectory.data.network

// DTO = Data Transfer Object. This models the network response.
data class UserNetworkDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String
    // We can ignore the 'address' and 'company' objects for this assignment
)