package com.saudigitus.support_module.data.local.database

import androidx.room.*
import androidx.room.Database
import com.saudigitus.support_module.data.models.manuals.ManualItem

@Database(
    entities = [ManualItem::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun manualsDAO(): ManualsDao

    companion object {
        const val DB_NAME = "manuals_db"
    }
}
