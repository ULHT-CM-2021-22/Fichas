package com.github.mstavares.cm.fichas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
class PhotoRoom(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "photo_encoded") val photoEncoded: String)