package uk.ac.tees.mad.memorylog.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import java.time.LocalDate
import javax.inject.Inject

data class MemoryDay(
    val date: LocalDate,
    val hasMemory: Boolean,
    val thumbnailUrl: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository
) : ViewModel() {

    private val _memoryDays = MutableStateFlow<List<MemoryDay>>(emptyList())
    val memoryDays: StateFlow<List<MemoryDay>> = _memoryDays

    init {
        loadCalendarData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCalendarData() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val daysInMonth = today.lengthOfMonth()

            val daysList = (1..daysInMonth).map { day ->
                val dateStr = today.withDayOfMonth(day).toString()
                val memory = memoryRepository.getMemoryForDate(dateStr) // fetch from local DB
                MemoryDay(
                    date = today.withDayOfMonth(day),
                    hasMemory = memory != null,
                    thumbnailUrl = memory?.imagePath
                )
            }
            _memoryDays.value = daysList
        }
    }

    fun refresh() {
        loadCalendarData() // call this after adding a memory
    }
}