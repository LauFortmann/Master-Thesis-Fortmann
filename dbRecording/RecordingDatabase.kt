package com.example.beautifulmind.dbRecording

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities=[Recording::class], version=1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecordingDatabase: RoomDatabase(){
    abstract val recordingDao: RecordingDao

    companion object{

        @Volatile
        private var INSTANCE:RecordingDatabase?=null
        fun getInstance(context:Context):RecordingDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecordingDatabase::class.java,
                        "recording_data_database"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}


