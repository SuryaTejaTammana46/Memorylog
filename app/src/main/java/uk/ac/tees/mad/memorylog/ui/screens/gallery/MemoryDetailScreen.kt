package uk.ac.tees.mad.memorylog.ui.screens.gallery

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.R
import uk.ac.tees.mad.memorylog.ui.viewmodel.MemoryViewModel
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme

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
            contentDescription = memory!!.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.ic_image_placeholder),
            error = painterResource(id = R.drawable.ic_broken_image)
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


@Preview(showBackground = true, name = "MemoryLog – Memory Detail Screen")
@Composable
fun MemoryDetailScreenPreview() {
    MemoryLogTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1516589178581-6cd7833ae3b2?w=800",
                contentDescription = "Beautiful memory",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Bottom Action Buttons
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Share")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}