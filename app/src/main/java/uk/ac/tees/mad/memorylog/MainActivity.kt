package uk.ac.tees.mad.memorylog

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.memorylog.navigation.AppNavGraph
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme
import uk.ac.tees.mad.memorylog.ui.viewmodel.ThemeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val darkTheme by themeViewModel.isDarkTheme.collectAsState()

            MemoryLogTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}

