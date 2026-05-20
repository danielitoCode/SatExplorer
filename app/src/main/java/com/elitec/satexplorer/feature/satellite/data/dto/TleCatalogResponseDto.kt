package com.elitec.satexplorer.feature.satellite.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TleCatalogResponseDto(
    val totalItems: Int,
    val member: List<TleCatalogItemDto> = emptyList(),
    val parameters: TleCatalogParametersDto,
    val view: TleCatalogViewDto? = null
)

@Serializable
data class TleCatalogItemDto(
    val satelliteId: Int,
    val name: String,
    val date: String,
    val line1: String,
    val line2: String
)

@Serializable
data class TleCatalogParametersDto(
    val page: Int,
    @SerialName("page-size")
    val pageSize: Int
)

@Serializable
data class TleCatalogViewDto(
    val next: String? = null
)
