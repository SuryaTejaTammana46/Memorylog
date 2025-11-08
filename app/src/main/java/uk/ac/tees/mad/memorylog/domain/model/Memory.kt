package uk.ac.tees.mad.memorylog.domain.model

import java.time.LocalDate

data class Memory(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "" // ISO string for now
)
