package uk.ac.tees.mad.memorylog.ui.screens.calender.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.memorylog.ui.viewmodel.MemoryDay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDayItem(
    memoryDay: MemoryDay,
    modifier: Modifier = Modifier,
    onClick: (MemoryDay) -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(
                if (memoryDay.hasMemory) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else Color.Transparent,
                MaterialTheme.shapes.medium
            )
            .clickable { onClick(memoryDay) },
        contentAlignment = Alignment.Center
    ) {
        if (memoryDay.hasMemory && memoryDay.thumbnailUrl != null) {
            coil.compose.AsyncImage(
                model = memoryDay.thumbnailUrl,
                contentDescription = "Memory thumbnail",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = memoryDay.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}