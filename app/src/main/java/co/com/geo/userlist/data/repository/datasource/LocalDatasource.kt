package co.com.geo.userlist.data.repository.datasource

import android.net.Uri
import co.com.geo.userlist.data.db.UserDatabase
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.data.model.UserEntitySeven
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class LocalDatasource(val userDatabase: UserDatabase): UserDataSource {

    override fun getUserList(): Flowable<List<UserEntity>> =
        userDatabase
            .getUserDao()
            .getAllUsers()
            .toFlowable()

    fun saveUsers(users: List<UserEntity>) {
        Observable.fromCallable {
            userDatabase.getUserDao().insertAll(users)
        }
        .subscribeOn(Schedulers.io())
        .subscribe()
    }

    fun insertEditOne(userEntity: UserEntity) = Observable.fromCallable {
        userDatabase.getUserDao().insertEditOne(userEntity)
    }

    fun loadOneByIdDB(uuid: String) = Observable.fromCallable {
        val savedUser = userDatabase.getUserDao().findOneById(uuid)
        savedUser
    }

    fun getAllUsersBy(name: String) =
        userDatabase
            .getUserDao()
            .getAllUsersBy(name)
            .toFlowable()

    fun getAllUserFavorite() =
        userDatabase
            .getUserDao()
            .getAllUserFavorite()
            .toFlowable()

}