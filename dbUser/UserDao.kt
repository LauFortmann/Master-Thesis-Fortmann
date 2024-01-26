package com.example.beautifulmind.dbUser

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beautifulmind.dbUser.User


@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user_data_table")
    fun getAllUser(): LiveData<List<User>>


}