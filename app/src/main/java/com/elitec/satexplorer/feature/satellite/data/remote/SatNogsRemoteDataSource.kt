package com.elitec.satexplorer.feature.satellite.data.remote

import com.elitec.satexplorer.feature.satellite.data.dto.SatNogsSatelliteDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class SatNogsRemoteDataSource(
    private val httpClient: HttpClient,
    private val json: Json
) {

    suspend fun fetchByNorad(noradId: Int): SatNogsSatelliteDto? {
        val response = httpClient
            .get("https://db.satnogs.org/api/satellites/?norad_cat_id=$noradId")
            .bodyAsText()

        return json.decodeFromString<List<SatNogsSatelliteDto>>(response).firstOrNull()
    }
}
