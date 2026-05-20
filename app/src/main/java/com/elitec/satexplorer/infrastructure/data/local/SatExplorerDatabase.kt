package com.elitec.satexplorer.infrastructure.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elitec.satexplorer.infrastructure.data.local.dao.SatelliteCacheDao
import com.elitec.satexplorer.infrastructure.data.local.entity.SatelliteCacheEntity

@Database(
    entities = [SatelliteCacheEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SatExplorerDatabase : RoomDatabase() {
    abstract fun satelliteCacheDao(): SatelliteCacheDao
}
