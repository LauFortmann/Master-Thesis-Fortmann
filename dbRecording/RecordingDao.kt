package com.example.beautifulmind.dbRecording

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beautifulmind.dbUser.User


@Dao
interface RecordingDao{

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertRecording(recording: Recording)

@Update
suspend fun updateRecording(recording: Recording)

@Delete
suspend fun deleteRecording(recording: Recording)

@Query("SELECT * FROM recording_data_table")
fun getAllRecordings(): LiveData<List<Recording>>

@Query("SELECT * FROM recording_data_table ORDER BY recording_id DESC LIMIT 1")
 fun getLastRecording():LiveData<Recording>
}

