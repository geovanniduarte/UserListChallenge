package co.com.geo.userlist.data.repository.datasource

import co.com.geo.userlist.data.model.UserEntity
import io.reactivex.Flowable


interface UserDataSource {
    fun getUserList() : Flowable<List<UserEntity>>
}
