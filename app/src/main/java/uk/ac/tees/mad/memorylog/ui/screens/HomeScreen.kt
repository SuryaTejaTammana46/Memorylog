package uk.ac.tees.mad.memorylog.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.memorylog.ui.screens.calender.CalendarScreen
import uk.ac.tees.mad.memorylog.ui.screens.gallery.GalleryScreen

sealed class BottomNavItem(val route: String, val label: String) {
    object Calendar : BottomNavItem("calendar", "Calendar")
    object Gallery : BottomNavItem("gallery", "Gallery")
    object Profile : BottomNavItem("profile", "Profile")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Calendar) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(BottomNavItem.Calendar, BottomNavItem.Gallery, BottomNavItem.Profile).forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab.route == item.route,
                        onClick = { selectedTab = item },
                        label = { Text(item.label) },
                        icon = { /* Add icon here if you want */ }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Calendar.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Calendar.route) {
                CalendarScreen(
                    onAddMemoryClick = { date ->
                        navController.navigate("capture/$date")
                    },
                    onDayClick = { date ->
                        // Navigate to memory detail if needed
                    }
                )
            }
            composable(BottomNavItem.Gallery.route) {
                GalleryScreen(
                    onMemoryClick = { memoryId ->
                        navController.navigate("memory_detail/$memoryId")
                    }
                )
            }
            composable(BottomNavItem.Profile.route) {
                // Profile screen content
                Text("Profile / Settings Screen")
            }
        }
    }
}
