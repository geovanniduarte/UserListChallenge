package co.com.geo.userlist.data.db

import android.arch.persistence.room.*
import co.com.geo.userlist.data.model.UserEntity
import io.reactivex.Maybe

@Dao
abstract class UserDao {

    @Query("SELECT * FROM users")
    abstract fun getAllUsers(): Maybe<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(list: List<UserEntity>)

    @Query("DELETE FROM users")
    abstract fun deleteAllUsers()

    @Transaction
    open fun removeAndInsertUsers(users: List<UserEntity>) {
        deleteAllUsers()
        insertAll(users)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertEditOne(movieEntity: UserEntity): Long

    @Query("SELECT * FROM users WHERE id = :uuid")
    abstract fun findOneById(uuid: String) : UserEntity

    @Query("SELECT name FROM users WHERE name like :name")
    abstract fun getAllUsersBy(name: String) : Maybe<List<String>>

    @Query("SELECT * FROM users WHERE favorite = 1")
    abstract fun getAllUserFavorite() : Maybe<List<UserEntity>>
}