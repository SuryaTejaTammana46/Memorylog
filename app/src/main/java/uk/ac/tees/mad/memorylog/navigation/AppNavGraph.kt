package uk.ac.tees.mad.memorylog.navigation

import CaptureScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.memorylog.ui.screens.auth.LoginScreen
import uk.ac.tees.mad.memorylog.ui.screens.auth.SignupScreen
import uk.ac.tees.mad.memorylog.ui.screens.calender.CalendarScreen
import uk.ac.tees.mad.memorylog.ui.screens.gallery.GalleryScreen
import uk.ac.tees.mad.memorylog.ui.screens.gallery.MemoryDetailScreen
import uk.ac.tees.mad.memorylog.ui.screens.memory.AddMemoryScreen
import uk.ac.tees.mad.memorylog.ui.screens.memory.PreviewMemoryScreen
import uk.ac.tees.mad.memorylog.ui.screens.splash.SplashScreen
import java.time.LocalDate


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object AddMemory : Screen("add_memory/{date}?photoPath={photoPath}") {
        fun route(date: String, photoPath: String) = "add_memory/$date?photoPath=$photoPath"
    }
    object Calendar : Screen("calendar")
    //    object TestLocalDb : Screen("testLocalDb")
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
            SplashScreen(onNavigateNext = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Calendar.route) {
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
                    navController.navigate(Screen.Calendar.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onDayClick = { selectedDate ->
                    // later: open memory detail if exists
                },
                onAddMemoryClick = { selectedDate ->
                    navController.navigate(Screen.Capture.route(selectedDate))
                }
            )
        }
        composable("addMemory/{date}") { navBackStackEntry ->
            val date = navBackStackEntry.arguments?.getString("date") ?: ""
            val photoPath = navBackStackEntry.arguments?.getString("photoPath") ?: ""

            AddMemoryScreen(
                date = date,
                photoPath = photoPath,
                onMemoryAdded = {
                    navController.navigate("gallery") {
                        popUpTo("addMemory/{date}/{photoPath}") { inclusive = true }
                    }
                }
            )
        }
//        composable(Screen.TestLocalDb.route) {
//            TestLocalDbScreen()
//        }
        composable("capture/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()

            CaptureScreen(onPhotoSaved = { path ->
                navController.navigate("addMemory/${LocalDate.now()}/$path")
            })
        }
        composable("preview_memory?path={path}") { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path") ?: ""
            PreviewMemoryScreen(
                photoPath = path,
                onRetake = { navController.popBackStack() },
                onConfirm = { navController.navigate("add_memory?date=${LocalDate.now()}&photoPath=$path") }
            )
        }
        composable(Screen.Gallery.route) {
            GalleryScreen(
                onMemoryClick = { memoryId ->
                    navController.navigate(Screen.MemoryDetail.route(memoryId))
                }
            )
        }

        composable(Screen.MemoryDetail.route) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getString("id") ?: ""
            MemoryDetailScreen(
                memoryId = memoryId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}