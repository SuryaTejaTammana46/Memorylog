package uk.ac.tees.mad.memorylog.ui.screens.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import uk.ac.tees.mad.memorylog.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.viewmodel.MemoryViewModel

@Composable
fun GalleryScreen(
    viewModel: MemoryViewModel = hiltViewModel(),
    onMemoryClick: (String) -> Unit
) {
    val memories = viewModel.allMemories.value
    val search = viewModel.searchQuery.value
    val sortNewest = viewModel.sortNewestFirst.value

    LaunchedEffect(Unit) { viewModel.loadGallery() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = search,
                onValueChange = { viewModel.searchMemories(it) },
                label = { Text("Search memories...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            Button(onClick = { viewModel.toggleSort() }) {
                Text(if (sortNewest) "Newest" else "Oldest")
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(memories) { memory ->
                GalleryMemoryCard(memory) { onMemoryClick(memory.id) }
            }
        }
    }
}

@Composable
fun GalleryMemoryCard(memory: Memory, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = memory.imagePath,
            contentDescription = memory.title,
            modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_image_placeholder),
            error = painterResource(id = R.drawable.ic_broken_image)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(memory.title, style = MaterialTheme.typography.titleMedium)
            Text(memory.date, style = MaterialTheme.typography.bodyMedium)
            Text(memory.description, maxLines = 1, color = Color.Gray)
        }
    }
}


@Preview(showBackground = true, name = "MemoryLog – Gallery Screen")
@Composable
fun GalleryScreenPreview() {
    val sampleMemories = listOf(
        Memory(
            id = "1",
            title = "Summer Beach Trip 2024",
            date = "15 July 2024",
            description = "Amazing day with friends at Saltburn beach!",
            imagePath = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800"
        ),
        Memory(
            id = "2",
            title = "First Day at University",
            date = "18 September 2023",
            description = "Excited and nervous — new chapter begins!",
            imagePath = "https://images.unsplash.com/photo-1523050858588-9cb708a54e9f?w=800"
        ),
        Memory(
            id = "3",
            title = "Graduation Day",
            date = "10 July 2024",
            description = "Finally made it! So proud of this moment.",
            imagePath = "https://images.unsplash.com/photo-1523580494863-6f3031224c94?w=800"
        ),
        Memory(
            id = "4",
            title = "Winter Wonderland",
            date = "25 December 2023",
            description = "Christmas morning with family — pure magic.",
            imagePath = "https://images.unsplash.com/photo-1543589077-57900d2634da?w=800"
        )
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = "beach",
                onValueChange = {},
                label = { Text("Search memories...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.width(12.dp))
            Button(onClick = {}) {
                Text("Newest")
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sampleMemories) { memory ->
                GalleryMemoryCard(memory = memory, onClick = {})
            }
        }
    }
}

// Required data class for preview only
private data class Memory(
    val id: String,
    val title: String,
    val date: String,
    val description: String,
    val imagePath: String
)
