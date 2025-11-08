package uk.ac.tees.mad.memorylog.ui.screens.uistate

import uk.ac.tees.mad.memorylog.domain.model.Memory

data class CalendarUiState(
    val memories: List<Memory> = emptyList(),
    val selectedDate: String? = null,
    val loading: Boolean = false,
    val error: String? = null
)