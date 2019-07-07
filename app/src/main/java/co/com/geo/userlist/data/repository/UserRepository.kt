package co.com.geo.userlist.data.repository

import android.net.Uri
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.data.model.UserEntitySeven
import co.com.geo.userlist.data.repository.datasource.ApiDataSource
import co.com.geo.userlist.data.repository.datasource.LocalDatasource
import co.com.geo.userlist.data.repository.datasource.UserFakeDataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import java.io.File
import java.util.concurrent.TimeUnit

class UserRepository(private val localDatasource: LocalDatasource,
                     private val apiDataSource: ApiDataSource) {

    fun getUserList(): Flowable<List<UserEntity>> =
           // getUsersFromDb().concatWith(getUsersFromApi())
        getUsersFromApi()

    private fun getUsersFromDb() : Flowable<List<UserEntity>> = localDatasource.getUserList()

    private fun getUsersFromApi() : Flowable<List<UserEntity>> = apiDataSource.getUserList()
        .doOnNext { localDatasource.saveUsers(it) }

    fun insertEditOne(userEntity: UserEntity) = localDatasource.insertEditOne(userEntity)

    fun loadOneDB(uuid: String) = localDatasource.loadOneByIdDB(uuid)

    fun getAllUsersBy(name: String) = localDatasource.getAllUsersBy(name)

    fun getAllUserFavorite() = localDatasource.getAllUserFavorite()

}