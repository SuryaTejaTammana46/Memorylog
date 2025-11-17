package uk.ac.tees.mad.memorylog.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.memorylog.viewmodel.TestLocalDbViewModel

@Composable
fun TestLocalDbScreen(viewModel: TestLocalDbViewModel = hiltViewModel()) {
    val memories = viewModel.memories.collectAsState()

    Column {
        Button(onClick = { viewModel.insertTestMemory() }) {
            Text("Insert Test Memory")
        }

        Text("Stored Memories:")
        memories.value.forEach {
            Text("${it.date} - ${it.title}")
        }
    }
}