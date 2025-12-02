package uk.ac.tees.mad.memorylog.ui.screens.gallery

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.viewmodel.MemoryViewModel

@Composable
fun MemoryDetailScreen(
    memoryId: String,
    viewModel: MemoryViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // load memory when composable is shown
    LaunchedEffect(memoryId) {
        viewModel.loadMemoryByDate(memoryId)
    }

    val memory by viewModel.selectedMemory.collectAsState()

    if (memory == null) {
        // optionally show a loader or empty state
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
    }

    Box(Modifier.fillMaxSize()) {
        AsyncImage(
            model = memory!!.imagePath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                // Share
                val uri = Uri.parse(memory!!.imagePath)
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share memory"))
            }) {
                Text("Share")
            }

            Button(onClick = {
                coroutineScope.launch {
                    viewModel.deleteMemory(memory!!.date) {
                        onBack()
                    }
                }
            }) {
                Text("Delete")
            }
        }
    }
}
