package uk.ac.tees.mad.memorylog.previews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.memorylog.R
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme

@Composable
fun SplashScreen_UI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(1f)     // full vis, animation removed for preview
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "MemoryLog",
                fontSize = 28.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_SplashScreen_UI() {
    MemoryLogTheme {
        SplashScreen_UI()
    }
}
