package com.elitec.satexplorer.feature.satellite.data.remote

import com.elitec.satexplorer.feature.satellite.data.dto.TleCatalogResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import java.net.URLEncoder

class TleCatalogRemoteDataSource(
    private val httpClient: HttpClient,
    private val json: Json
) {

    suspend fun searchSatellites(
        query: String,
        page: Int,
        pageSize: Int
    ): TleCatalogResponseDto {
        val encodedQuery = URLEncoder.encode(query.ifBlank { "*" }, Charsets.UTF_8.name())
        val response = httpClient.get(
            "https://tle.ivanstanojevic.me/api/tle/?search=$encodedQuery&page=$page&page-size=$pageSize"
        ).bodyAsText()

        return json.decodeFromString<TleCatalogResponseDto>(response)
    }
}
