package uk.ac.tees.mad.memorylog.navigation

import CaptureScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uk.ac.tees.mad.memorylog.ui.screens.auth.LoginScreen
import uk.ac.tees.mad.memorylog.ui.screens.auth.SignupScreen
import uk.ac.tees.mad.memorylog.ui.screens.gallery.MemoryDetailScreen
import uk.ac.tees.mad.memorylog.ui.screens.memory.AddMemoryScreen
import uk.ac.tees.mad.memorylog.ui.screens.memory.PreviewMemoryScreen
import uk.ac.tees.mad.memorylog.ui.screens.splash.SplashScreen
import java.time.LocalDate


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Settings : Screen("settings")
    object Calendar : Screen("calendar")
    object Home : Screen("home")

    //    object TestLocalDb : Screen("testLocalDb")
    object AddMemory : Screen("add_memory/{date}?photoPath={photoPath}") {
        fun route(date: String, photoPath: String) = "add_memory/$date?photoPath=$photoPath"
    }

    object Capture : Screen("capture/{date}") {
        fun route(date: String) = "capture/$date"
    }

    object Gallery : Screen("gallery")
    object MemoryDetail : Screen("memory_detail/{id}") {
        fun route(id: String) = "memory_detail/$id"
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
//        composable(Screen.Calendar.route) {
//            CalendarScreen(
//                onDayClick = { selectedDate ->
//                    // later: open memory detail if exists
//                },
//                onAddMemoryClick = { selectedDate ->
//                    navController.navigate(Screen.Capture.route(selectedDate))
//                }
//            )
//        }
        composable(
            route = "add_memory/{date}?photoPath={photoPath}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType },
                navArgument("photoPath") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val photoPath = backStackEntry.arguments?.getString("photoPath") ?: ""

            AddMemoryScreen(
                date = date,
                photoPath = photoPath,
                onMemoryAdded = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.navigate(Screen.Calendar.route) }
            )
        }

        composable(Screen.Capture.route) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
            CaptureScreen(
                onPhotoSaved = { path ->
                    navController.navigate(Screen.AddMemory.route(date, path))
                }
            )
        }

//        composable(Screen.TestLocalDb.route) {
//            TestLocalDbScreen()
//        }
        composable("preview_memory?path={path}") { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path") ?: ""
            PreviewMemoryScreen(
                photoPath = path,
                onRetake = { navController.popBackStack() },
                onConfirm = { navController.navigate("add_memory?date=${LocalDate.now()}&photoPath=$path") }
            )
        }
//        composable(Screen.Gallery.route) {
//            GalleryScreen(
//                onMemoryClick = { memoryId ->
//                    navController.navigate(Screen.MemoryDetail.route(memoryId))
//                }
//            )
//        }

        composable(Screen.MemoryDetail.route) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getString("id") ?: ""
            MemoryDetailScreen(
                memoryId = memoryId,
                onBack = { navController.popBackStack() }
            )
        }
//        composable(Screen.Settings.route) {
//            SettingsScreen()
//        }
    }
}