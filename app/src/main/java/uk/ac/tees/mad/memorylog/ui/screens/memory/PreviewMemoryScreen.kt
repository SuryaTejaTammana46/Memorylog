package uk.ac.tees.mad.memorylog.ui.screens.memory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PreviewMemoryScreen(
    photoPath: String,
    onRetake: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {

        AsyncImage(
            model = photoPath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onRetake, modifier = Modifier.weight(1f)) {
                Text("Retake")
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = onConfirm, modifier = Modifier.weight(1f)) {
                Text("Use Photo")
            }
        }
    }
}
