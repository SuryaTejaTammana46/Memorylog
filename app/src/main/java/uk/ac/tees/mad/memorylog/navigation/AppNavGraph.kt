package uk.ac.tees.mad.memorylog.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.memorylog.ui.screens.auth.LoginScreen
import uk.ac.tees.mad.memorylog.ui.screens.auth.SignupScreen
import uk.ac.tees.mad.memorylog.ui.screens.calender.CalendarScreen
import uk.ac.tees.mad.memorylog.ui.screens.memory.AddMemoryScreen
import uk.ac.tees.mad.memorylog.ui.screens.splash.SplashScreen
import uk.ac.tees.mad.memorylog.ui.screens.TestLocalDbScreen
import uk.ac.tees.mad.memorylog.ui.screens.capture.CaptureScreen
import java.time.LocalDate


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object AddMemory : Screen("add_memory/{date}?photoPath={photoPath}") {
        fun route(date: String, photoPath: String) = "add_memory/$date?photoPath=$photoPath"
    }
    object Calendar : Screen("calendar")
    object TestLocalDb : Screen("testLocalDb")
    object Capture : Screen("capture/{date}") {
        fun route(date: String) = "capture/$date"
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateNext = {
                navController.navigate(Screen.Capture.route) {
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
        composable("add_memory/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
            val photoPath = backStackEntry.arguments?.getString("photoPath") ?: ""

            AddMemoryScreen(
                date = date,
                onMemoryAdded = { navController.popBackStack() },
                photoPath = photoPath
            )
        }
        composable(Screen.TestLocalDb.route) {
            TestLocalDbScreen()
        }
        composable("capture/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()

            CaptureScreen(onPhotoSaved = { photoPath ->
                navController.navigate(Screen.AddMemory.route(date, photoPath)) {
                    popUpTo(Screen.Capture.route(date)) { inclusive = true }
                }
            })
        }


    }
}