package com.github.mstavares.cm.fichas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {

    @Insert
    suspend fun insert(photo: PhotoRoom)

    @Query("SELECT * FROM photo ORDER BY id ASC")
    suspend fun getAll(): List<PhotoRoom>
}
