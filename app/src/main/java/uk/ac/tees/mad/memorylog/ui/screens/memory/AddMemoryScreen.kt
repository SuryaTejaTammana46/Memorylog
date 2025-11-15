package uk.ac.tees.mad.memorylog.ui.screens.memory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import uk.ac.tees.mad.memorylog.viewmodel.MemoryViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddMemoryScreen(
    viewModel: MemoryViewModel = hiltViewModel(),
    onMemoryAdded: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showReplaceDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            onMemoryAdded()
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
                            date = LocalDate.now().toString()
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
                        date = LocalDate.now().toString()
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
