package com.saudigitus.support_module.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saudigitus.support_module.data.models.manuals.ManualItem

@Dao
interface ManualsDao {
    @Query("SELECT * FROM manualItem")
    fun getAll(): List<ManualItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(mediaDetails: ManualItem)

    @Query("SELECT * FROM manualItem WHERE uid LIKE :id")
    fun getDetailsById(id: String): ManualItem

    @Delete
    fun delete(manual: ManualItem)
}
