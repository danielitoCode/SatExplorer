package com.elitec.satexplorer.feature.analitics.presentation.model

data class LiveLogItem(
    val id: Long,
    val tittle: String,
    val body: String,
    val type: LogType,
    val time: String
)
