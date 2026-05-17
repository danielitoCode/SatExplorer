package com.elitec.satexplorer.feature.tracking.data.remote

import com.elitec.satexplorer.feature.tracking.data.dto.CelestrakTleDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class CelestrakRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchTle(noradId: Int): CelestrakTleDto {
        val raw = httpClient.get("https://celestrak.org/NORAD/elements/gp.php?CATNR=$noradId&FORMAT=TLE")
            .bodyAsText()
            .trim()
        val lines = raw.lines().filter { it.isNotBlank() }
        require(lines.size >= 3) { "No se encontró TLE válido para NORAD $noradId" }
        return CelestrakTleDto(lines[0].trim(), lines[1].trim(), lines[2].trim())
    }
}