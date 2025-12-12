package uk.ac.tees.mad.memorylog.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import uk.ac.tees.mad.memorylog.R
import uk.ac.tees.mad.memorylog.ui.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val viewModel: SplashViewModel = hiltViewModel()
    val imageUrl by viewModel.dailyImage.collectAsState()
    val quote by viewModel.dailyQuote.collectAsState()

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(if (startAnimation) 1f else 0f, label = "")

    LaunchedEffect(true) {
        startAnimation = true
        viewModel.fetchDailyInspiration()
        delay(2000)

        // Check Firebase authentication state
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Daily inspiration",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.playstore_icon),
                alpha = 0.2f
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.playstore_icon),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alphaAnim.value)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "MemoryLog",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alpha(alphaAnim.value)
            )
            Spacer(Modifier.height(12.dp))
            quote?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .alpha(alphaAnim.value)
                )
            }
        }
    }
}