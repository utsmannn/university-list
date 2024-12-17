package com.utsman.universitylist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "university")
data class UniversityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val domain: String,
    @ColumnInfo(name = "web_page")
    val webPage: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)