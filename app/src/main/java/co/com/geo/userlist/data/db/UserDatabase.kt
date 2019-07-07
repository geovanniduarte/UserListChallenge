package co.com.geo.userlist.data.db


import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import co.com.geo.userlist.data.model.UserEntity

@Database(entities = [UserEntity::class], version = 5)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao() : UserDao

}