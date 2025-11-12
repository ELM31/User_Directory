// ui/UserViewModel.kt
package com.example.userdirectory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userdirectory.data.UserRepository
import com.example.userdirectory.data.local.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // A StateFlow to hold the current search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // This flow is the core of the UI state.
    // It uses flatMapLatest to "react" to changes in the search query.
    // When the query changes, it "flattens" to a new Flow from the repository.
    @OptIn(ExperimentalCoroutinesApi::class)
    val users: StateFlow<List<UserEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                // If query is blank, show all users
                repository.allUsers
            } else {
                // Otherwise, show search results
                repository.searchUsers(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), // Keep flow active 5s after last collector
            initialValue = emptyList() // Start with an empty list
        )

    init {
        // When the ViewModel is created, trigger a background refresh.
        // This fulfills Step 2 of the offline-first pattern.
        // The UI is already observing 'users' (Step 1).
        viewModelScope.launch {
            repository.refreshUsers()
        }
    }

    // Called by the UI when the user types in the search bar
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}