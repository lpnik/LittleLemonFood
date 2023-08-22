package com.example.littlelemonfood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.littlelemonfood.composables.OnboardingScreen
import com.example.littlelemonfood.ui.theme.LittleLemonFoodTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.littlelemonfood.composables.Destinations
import com.example.littlelemonfood.composables.HomeScreen
import com.example.littlelemonfood.composables.ProfileScreen
import com.example.littlelemonfood.composables.UserDataViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val userDataViewModel: UserDataViewModel by viewModels()

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "appDatabase").build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LittleLemonFoodTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = Destinations.Onboarding) {
                    composable(Destinations.Onboarding) {
                        OnboardingScreen(navController, userDataViewModel)
                    }
                    composable(Destinations.Home) {
                        HomeScreen(navController, database)
                    }
                    composable(Destinations.Profile) {
                        ProfileScreen(navController, userDataViewModel)
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO){
            if(database.menuItemDao().isEmpty()) {
                val menuItems = fetchMenu()
                saveMenuItemsToDatabase(menuItems)
            }
        }
    }

    private suspend fun fetchMenu(): List<MenuItemNetwork> {
        return httpClient
            .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            .body<MenuNetworkdata>()
            .menu
    }

    private fun saveMenuItemsToDatabase(menuItems: List<MenuItemNetwork>){
        val menuItemsToRoom = menuItems.map { it.toMenuItemRoom() }
        database.menuItemDao().insertAll(*menuItemsToRoom.toTypedArray())
    }
}