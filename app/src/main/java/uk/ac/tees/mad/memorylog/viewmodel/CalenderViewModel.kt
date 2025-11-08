package uk.ac.tees.mad.memorylog.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class MemoryDay(
    val date: LocalDate,
    val hasMemory: Boolean,
    val thumbnailUrl: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel : ViewModel() {

    private val _memoryDays = MutableStateFlow<List<MemoryDay>>(emptyList())
    val memoryDays: StateFlow<List<MemoryDay>> = _memoryDays

    init {
        loadSampleData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadSampleData() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val sample = (1..30).map { day ->
                MemoryDay(
                    date = today.withDayOfMonth(day),
                    hasMemory = day % 5 == 0,
                    thumbnailUrl = null
                )
            }
            _memoryDays.value = sample
        }
    }
}