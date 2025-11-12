# User Directory App

**Contributors:**

Eric Lopez Morales

# **User Directory App – README**

## **Project Overview**
**User Directory** is an Android application built using **Jetpack Compose** that implements a robust **Offline-First Architecture** pattern. Its core function is to fetch a list of users from a public API, cache that data locally using Room, and display it to the user while always prioritizing the local source of truth.

The app ensures a seamless user experience, allowing users to browse and search the directory even when the network connection is unavailable.

---

## **App Layout**

<img width="363" height="631" alt="image_2025-11-11_214450935" src="https://github.com/user-attachments/assets/5e1cb637-927b-47b6-a056-ccddd549a6e1" />


This app contains a single main screen demonstrating the core architecture concepts:

- **User List Screen**  
  Displays all users fetched and cached locally.  
  Users can:
  - Instantly view cached data upon launch.
  - Search the list by **Name** or **Email**.

- **Offline Support**  
  The application operates entirely on cached data when a network connection is lost, maintaining full functionality for browsing and searching the existing directory.

The **TopAppBar** provides a clean title and visual consistency.

---

## **Core Functionalities & Key Architectural Concepts**

| **Feature** | **Implementation Concept** |
| :--- | :--- |
| **Offline-First Architecture** | The **`UserRepository`** acts as the single source of truth, reading data **only** from the local **Room** database via a **`Flow`**. Network fetching (`Retrofit`) is treated as a background sync operation to update the cache. |
| **Data Synchronization** | Upon app launch, the **`UserViewModel`** immediately reads the cached data while simultaneously triggering **`repository.refreshUsers()`** to asynchronously fetch the latest data from the API and update Room. |
| **Local Search** | The search functionality is executed entirely within the **Room** database using a custom SQL **`LIKE`** query defined in the **`UserDao`**, ensuring no API calls are made during typing. |
| **Real-Time UI Updates** | All data in the UI is collected from a Kotlin **`Flow`** emitted by Room. Any data change (e.g., new data inserted from the API, or filtered results from search) causes the UI to update instantly. |

---

## **File Overview**

### **MainActivity.kt**
- Entry point for the app  
- Sets up the **theme** and initializes the primary Compose screen.

### **Data Layer**

#### **`UserRepository.kt`**
Manages data flow from the network (`ApiService`) and local cache (`UserDao`), ensuring data consistency and implementing the offline-first logic.

#### **`UserViewModel.kt`**
Manages app state including the displayed list of users and the search query.  
**Functions include:**
- **`onSearchQueryChanged(query: String)`** → Updates the search state.
- **`users`** → A StateFlow that switches between the full list and the filtered list based on the search query using **`flatMapLatest`**.

---

### **Local Database (Room)**

| **File** | **Role** |
| :--- | :--- |
| **`UserEntity.kt`** | Defines the schema for the local `users` table. |
| **`UserDao.kt`** | Contains SQL operations, including the `searchUsers` query and `insertAll` using **`OnConflictStrategy.REPLACE`**. |
| **`UserDatabase.kt`** | The main abstract class for Room configuration. |

---

### **Network (Retrofit)**

| **File** | **Role** |
| :--- | :--- |
| **`ApiService.kt`** | Retrofit interface defining the API call (`@GET("users")`). |
| **`UserNetworkDto.kt`** | Data class modeling the JSON structure from the API response. |

---

### **UI Components**
- **`UserScreen.kt`** → Displays the full list of users in a scrollable `LazyColumn`; handles the `OutlinedTextField` for search input.  
- **`UserListItem.kt`** → Displays a single user's **ID, Name, Email, and Phone Number**.
<img width="386" height="861" alt="image_2025-11-11_214616508" src="https://github.com/user-attachments/assets/7be1540f-ceb5-4f7d-889e-c636aa0a1a02" />

---

## **Functions and Concepts Used**
- **Jetpack Compose UI:** Declarative UI for building screens and components.  
- **Dependency Injection (Hilt):** Used to provide Singleton instances of Retrofit, Room, and the Repository across the application.  
- **Kotlin Coroutines and Flow:** Used extensively for asynchronous network calls and real-time data observation from the database.  
- **Offline-First Architecture:** The core design principle ensuring data resilience and responsiveness.

---
