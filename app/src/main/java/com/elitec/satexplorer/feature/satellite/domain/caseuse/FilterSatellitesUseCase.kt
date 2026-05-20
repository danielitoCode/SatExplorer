package com.elitec.satexplorer.feature.satellite.domain.caseuse

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogCategory
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite

class FilterSatellitesUseCase {
    operator fun invoke(
        satellites: List<Satellite>,
        query: String,
        selectedCategory: SatelliteCatalogCategory
    ): List<Satellite> {
        val normalizedQuery = query.trim()

        return satellites.filter { satellite ->
            val matchesQuery = normalizedQuery.isBlank() ||
                satellite.name.contains(normalizedQuery, ignoreCase = true) ||
                satellite.noradId.toString().contains(normalizedQuery)
            val matchesType = selectedCategory.matches(satellite)

            matchesQuery && matchesType
        }
    }
}
