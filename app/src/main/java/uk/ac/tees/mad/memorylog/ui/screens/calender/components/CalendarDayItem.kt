package uk.ac.tees.mad.memorylog.ui.screens.calender.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.memorylog.viewmodel.MemoryDay
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDayItem(
    memoryDay: MemoryDay,
    onClick: (MemoryDay) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("d")
    val backgroundColor =
        if (memoryDay.hasMemory) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        else {
            Color.Transparent
        }

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(backgroundColor, MaterialTheme.shapes.medium)
            .clickable { onClick(memoryDay) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = memoryDay.date.format(formatter),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}