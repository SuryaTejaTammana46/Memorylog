package uk.ac.tees.mad.memorylog.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.memorylog.ui.screens.calender.components.CalendarDayItem
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme
import uk.ac.tees.mad.memorylog.ui.viewmodel.MemoryDay
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen_UI() {
    val fake = (1..30).map {
        MemoryDay(
            date = LocalDate.of(2025, 1, it),
            hasMemory = it % 4 == 0
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {}) { Text("Add Memory") }

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fake) { day ->
                CalendarDayItem(memoryDay = day) {}
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun Preview_CalendarScreen_UI() {
    MemoryLogTheme {
        CalendarScreen_UI()
    }
}
