package com.dicoding.habitapp.data

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//TODO 1 : Define a local database table using the schema in app/schema/habits.json
@Parcelize
@Entity(tableName = "habits")
data class Habit(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    @NonNull
    val id: Int = 0,

    @ColumnInfo("title")
    @NonNull
    val title: String,

    @ColumnInfo("minutesFocus")
    @NonNull
    val minutesFocus: Long,

    @ColumnInfo("startTime")
    @NonNull
    val startTime: String,

    @ColumnInfo("priorityLevel")
    @NonNull
    val priorityLevel: String
): Parcelable
