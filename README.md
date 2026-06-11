# FoodPlannerApp

## Overview
FoodPlannerApp is a comprehensive Android application designed to help users discover, organize, and plan their daily meals. Powered by TheMealDB API, the app allows users to search for recipes, view detailed instructions along with video tutorials, save their favorite meals, and schedule their meals using a built-in weekly planner.

## Key Features
- **User Authentication:** Secure login and registration using Firebase Authentication and Google Sign-In.
- **Home Dashboard:** Browse random meals and explore various meal categories.
- **Search & Filter:** Search for specific meals by name, or filter them by category, geographic area (country), and ingredients.
- **Meal Details:** View comprehensive recipe details, including step-by-step instructions, ingredients list, and an embedded YouTube video player.
- **Favorites:** Save and manage favorite recipes locally for offline quick access.
- **Meal Planner:** Schedule and manage meals for specific days using a dedicated planner interface.
- **User Profile:** Manage user settings and authentication state.

## Tech Stack
- **Language:** Java
- **Frameworks & UI:** Android SDK, Material Design Components, ConstraintLayout, Navigation Component, Lottie (Animations).
- **Architecture:** MVP (Model-View-Presenter) with Repository Pattern.
- **Asynchronous & Reactive:** RxJava3, RxAndroid.
- **Networking:** Retrofit2, Gson Converter.
- **Local Database:** Room Database.
- **Image Loading:** Glide.
- **Media:** Android YouTube Player API.
- **Backend Services:** Firebase (Auth, Realtime Database, Analytics), Google Identity Services.
- **External API:** TheMealDB.

## Architecture
The application follows the **MVP (Model-View-Presenter)** architectural pattern combined with the **Repository Pattern**. 
- **View:** Contains the UI components (Fragments/Activities) responsible for rendering the UI and capturing user interactions.
- **Presenter:** Acts as a middleman, handling the business logic, processing user inputs from the View, and updating the UI accordingly.
- **Model / Data Layer:** Managed by the Repository pattern, it acts as a single source of truth, abstracting the data sources (Room for local storage and Retrofit for remote API calls) using RxJava for reactive data streams.

## Setup & Installation
Follow these steps to build and run the project locally:

1. **Clone the repository:**
   ```bash
   git clone <repository_url>
   ```
2. **Open the Project:**
   Launch Android Studio and select **Open**, then navigate to the cloned `FoodPlannerApp` directory.
3. **Sync Gradle:**
   Allow Android Studio to sync the project with Gradle files to download the required dependencies.
4. **Firebase Configuration:**
   Ensure the `google-services.json` file is present in the `app/` directory for Firebase services to function properly. (If not, create a project on the Firebase Console, add an Android app with the package name `com.example.foodplannerapp`, and download the generated `google-services.json` into the `app/` folder).
5. **Build and Run:**
   Select an emulator or connect a physical Android device, then click the **Run** button (`Shift + F10`) in Android Studio.
