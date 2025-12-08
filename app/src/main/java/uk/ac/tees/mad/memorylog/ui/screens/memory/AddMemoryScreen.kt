package uk.ac.tees.mad.memorylog.ui.screens.memory


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import uk.ac.tees.mad.memorylog.viewmodel.MemoryViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddMemoryScreen(
    date: String,
    viewModel: MemoryViewModel = hiltViewModel(),
    onMemoryAdded: () -> Unit,
    photoPath: String,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showReplaceDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            viewModel.resetEvent()
            onMemoryAdded()
            onNavigateBack()
        }
    }

    if (showReplaceDialog) {
        AlertDialog(
            onDismissRequest = { showReplaceDialog = false },
            title = { Text("Replace Today's Memory?") },
            text = {
                Text("You already have a memory for today. Do you want to replace it? The previous memory will be deleted.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showReplaceDialog = false
                    viewModel.replaceMemory(
                        Memory(
                            title = title,
                            description = description,
                            date = LocalDate.now().toString(),
                            imagePath = photoPath
                        )
                    )
                }) {
                    Text("Yes, Replace")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReplaceDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (photoPath.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxWidth()) {

                AsyncImage(
                    model = photoPath,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f) // matches capture screen
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = date,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(
                            Color.Black.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(6.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("What made today special?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.addMemoryWithCheck(
                    Memory(
                        title = title,
                        description = description,
                        date = LocalDate.now().toString(),
                        imagePath = photoPath
                    ),
                    onReplaceRequest = { showReplaceDialog = true }
                )
            },
            enabled = uiState !is UiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState is UiState.Loading) "Saving..." else "Save Memory")
        }
    }
}