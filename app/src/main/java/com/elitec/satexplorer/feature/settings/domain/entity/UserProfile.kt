package com.elitec.satexplorer.feature.settings.domain.entity

data class UserProfile(
    val userId: String,
    val username: String,
    val email: String?,
    val createdAt: Long
)
