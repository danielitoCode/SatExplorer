package com.elitec.satexplorer.feature.analitics.domain.entity

data class EventLog(
    val id: Long,
    val type: String,
    val timestamp: Long,
    val metadata: String
)
