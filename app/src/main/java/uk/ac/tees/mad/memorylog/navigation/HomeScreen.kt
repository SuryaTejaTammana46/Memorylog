package uk.ac.tees.mad.memorylog.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.memorylog.ui.screens.calender.CalendarScreen
import uk.ac.tees.mad.memorylog.ui.screens.gallery.GalleryScreen
import uk.ac.tees.mad.memorylog.ui.screens.settings.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(rootNavController: NavHostController) {
    val innerNavController = rememberNavController()

    val bottomItems = listOf(
        BottomNavItem("Calendar", Icons.Outlined.DateRange, "calendar"),
        BottomNavItem("Gallery", Icons.Outlined.FavoriteBorder, "gallery"),
        BottomNavItem("Settings", Icons.Outlined.Settings, "settings")
    )

    Scaffold(
        bottomBar = { BottomNavBar(navController = innerNavController, items = bottomItems) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
//            Box {
            NavHost(
                navController = innerNavController,
                startDestination = Screen.Calendar.route
            ) {
                composable(Screen.Calendar.route) {
                    CalendarScreen(
                        onDayClick = { selectedDate ->
                            // later: open memory detail if exists
                        },
                        onAddMemoryClick = { date, onMemoryAdded ->
                            rootNavController.navigate(Screen.Capture.route(date))
                        }
                )
            }
            composable(Screen.Gallery.route) {
                GalleryScreen(
                    onMemoryClick = { memoryId ->
                        innerNavController.navigate(Screen.MemoryDetail.route(memoryId))
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
//            }
    }
}
}