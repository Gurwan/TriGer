package com.example.undevezhtriger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brezhodex_section")
data class BrezhodexSection (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val listGer: MutableList<Long> = mutableListOf(),
)
