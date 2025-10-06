package com.nutriweek.inclusiverecip.data

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    fun findByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    fun count(): Int

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun findById(id: String): UserEntity?
}
