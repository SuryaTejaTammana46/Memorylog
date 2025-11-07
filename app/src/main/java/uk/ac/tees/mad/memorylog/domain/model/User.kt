package uk.ac.tees.mad.memorylog.domain.model

data class User (
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)