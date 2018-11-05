package com.raywenderlich.android.imet.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.raywenderlich.android.imet.data.model.People

@Dao
interface PeopleDao {

    // 1: Select All
    @Query("SELECT * FROM People ORDER BY id DESC")
    fun getAll(): LiveData<List<People>> // We added LiveData here

    // 2: Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(people: People)

    // 3: Delete
    @Query("DELETE FROM People")
    fun deleteAll()

    // 4: Select by id
    @Query("SELECT * FROM People WHERE id = :id")
    fun find(id: Int): LiveData<People>

    // 5: Find user by name
    @Query("SELECT * FROM People WHERE name LIKE '%' || :name || '%'")
    fun findBy(name: String): LiveData<List<People>>

    // 6: This is my effort to be able to delete a contact
    @Query("DELETE FROM People WHERE id = :id")
    fun delete(id: Int)

    @Delete
    fun deletePeople(people: People)

}
