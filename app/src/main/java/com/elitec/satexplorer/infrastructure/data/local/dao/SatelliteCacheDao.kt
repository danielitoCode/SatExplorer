package com.elitec.satexplorer.infrastructure.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elitec.satexplorer.infrastructure.data.local.entity.SatelliteCacheEntity

@Dao
interface SatelliteCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<SatelliteCacheEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: SatelliteCacheEntity)

    @Query("SELECT * FROM satellite_cache WHERE noradId = :noradId LIMIT 1")
    suspend fun getByNorad(noradId: Int): SatelliteCacheEntity?

    @Query(
        "SELECT * FROM satellite_cache " +
            "WHERE (:query = '' OR name LIKE '%' || :query || '%' OR CAST(noradId AS TEXT) LIKE '%' || :query || '%') " +
            "ORDER BY COALESCE(lastCatalogSync, lastOrbitSync, 0) DESC, name ASC LIMIT :limit OFFSET :offset"
    )
    suspend fun searchAll(query: String, limit: Int, offset: Int): List<SatelliteCacheEntity>

    @Query(
        "SELECT COUNT(*) FROM satellite_cache " +
            "WHERE (:query = '' OR name LIKE '%' || :query || '%' OR CAST(noradId AS TEXT) LIKE '%' || :query || '%')"
    )
    suspend fun countAll(query: String): Int

    @Query(
        "SELECT * FROM satellite_cache " +
            "WHERE type IN (:types) AND (:query = '' OR name LIKE '%' || :query || '%' OR CAST(noradId AS TEXT) LIKE '%' || :query || '%') " +
            "ORDER BY COALESCE(lastCatalogSync, lastOrbitSync, 0) DESC, name ASC LIMIT :limit OFFSET :offset"
    )
    suspend fun searchByTypes(query: String, types: List<String>, limit: Int, offset: Int): List<SatelliteCacheEntity>

    @Query(
        "SELECT COUNT(*) FROM satellite_cache " +
            "WHERE type IN (:types) AND (:query = '' OR name LIKE '%' || :query || '%' OR CAST(noradId AS TEXT) LIKE '%' || :query || '%')"
    )
    suspend fun countByTypes(query: String, types: List<String>): Int
}
