// data/network/ApiService.kt
package com.example.userdirectory.data.network

import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserNetworkDto>
}