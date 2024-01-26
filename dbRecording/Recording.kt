package com.example.beautifulmind.dbRecording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

//4.6.2 in master thesis
@Entity(tableName="recording_data_table")
data class Recording(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="recording_id")
    var id:Int?,

    @ColumnInfo(name="recording_time")
    var time:String?,

    @ColumnInfo(name="recording_voice")
    var recording_voice:String?,

    @ColumnInfo(name="recording_heartrate")
    var recording_heartrate:ArrayList<Double>,

    @ColumnInfo(name="recording_label")
    var recording_label:Int?


)

