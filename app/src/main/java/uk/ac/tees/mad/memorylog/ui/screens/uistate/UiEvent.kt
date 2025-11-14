package uk.ac.tees.mad.memorylog.ui.screens.uistate

import uk.ac.tees.mad.memorylog.domain.model.Memory

sealed class UiEvent {
    data class MemoryExists(val memory: Memory): UiEvent()
    object MemoryAdded : UiEvent()
    object MemoryReplaced : UiEvent()
}