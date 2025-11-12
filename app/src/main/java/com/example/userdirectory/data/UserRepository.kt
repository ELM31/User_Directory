// data/UserRepository.kt
package com.example.userdirectory.data

import android.util.Log
import com.example.userdirectory.data.local.UserDao
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Hilt will provide a single instance of this repository
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {

    //The "Single Source of Truth" flow. The UI will observe this.
    //It reads directly from the database.
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()

    // The search function. This also reads directly from the database.
    fun searchUsers(query: String): Flow<List<UserEntity>> {
        return userDao.searchUsers(query)
    }


     //This function fetches new data from the API and updates the local Room database.
     //The UI will automatically update thanks to the Flow from `allUsers`.
    suspend fun refreshUsers() {
        try {
            //Try to fetch fresh data
            val networkUsers = apiService.getUsers()

            //If successful, update Room Database
            val userEntities = networkUsers.map { it.toEntity() } // Map DTOs to Entities
            userDao.insertAll(userEntities)

        } catch (e: Exception) {
            // If API fails, do nothing.
            // The user still sees the cached data from Room.
            Log.e("UserRepository", "Failed to refresh users: ${e.message}")
        }
    }
}