package uk.ac.tees.mad.memorylog.ui.screens.calender

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.memorylog.ui.screens.calender.components.CalendarDayItem
import uk.ac.tees.mad.memorylog.ui.screens.calender.components.ProfileHeader
import uk.ac.tees.mad.memorylog.ui.viewmodel.CalendarViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onDayClick: (String) -> Unit = {},
    onAddMemoryClick: (String, () -> Unit) -> Unit
) {
    val memoryDays by viewModel.memoryDays.collectAsState()
    val currentMonth by viewModel.currentMonth.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCalendarData()
    }

    val monthName = currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val year = currentMonth.year

    val firstDayOfMonth = currentMonth.atDay(1)
    // Monday = 1 → index 0, ..., Sunday = 7 → index 6
    val startOffset = (firstDayOfMonth.dayOfWeek.value + 6) % 7

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ProfileHeader("Calendar")
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { viewModel.previousMonth() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$monthName $year",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekday headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(startOffset) {
                Box(modifier = Modifier.size(48.dp))
            }

            items(memoryDays) { day ->
                CalendarDayItem(memoryDay = day, modifier = Modifier.fillMaxWidth()) {
                    onDayClick(it.date.toString())
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
//            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    val todayStr = LocalDate.now().toString()
                    onAddMemoryClick(todayStr) {
                        viewModel.refresh()
                    }
                }
            ) {
                Text("Add Memory")
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewCalendarScreen() {
    MemoryLogTheme {
        CalendarScreen(
            onAddMemoryClick = { _, _ -> }
        )
    }
}
