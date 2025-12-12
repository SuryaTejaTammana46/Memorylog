package uk.ac.tees.mad.memorylog.ui.viewmodel

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
import java.time.YearMonth
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

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCalendarData(month: YearMonth = _currentMonth.value) {
        viewModelScope.launch {
            val daysInMonth = month.lengthOfMonth()

            val daysList = (1..daysInMonth).map { day ->
                val date = month.atDay(day)
                val dateStr = date.toString()
                val memory = memoryRepository.getMemoryForDate(dateStr)
                MemoryDay(
                    date = date,
                    hasMemory = memory != null,
                    thumbnailUrl = memory?.imagePath
                )
            }
            _memoryDays.value = daysList
        }
    }

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
        loadCalendarData()
    }

    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
        loadCalendarData()
    }

    fun refresh() {
        loadCalendarData() // call this after adding a memory
    }
}