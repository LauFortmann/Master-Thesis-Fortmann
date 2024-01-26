package com.example.beautifulmind.dbUser

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 4.6.1 in master thesis
@Entity(tableName="user_data_table")
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="user_id")
    var id:Int?,

    @ColumnInfo(name="user_name")
    var name:String?,

    @ColumnInfo(name="user_age")
    var age:Int?,

    @ColumnInfo(name="user_gender")
    var gender:Int?,

    @ColumnInfo(name="user_time")
    var time:String?,

)
