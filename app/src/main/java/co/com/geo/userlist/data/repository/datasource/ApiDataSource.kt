package co.com.geo.userlist.data.repository.datasource

import co.com.geo.userlist.data.mappers.UserEntityMapper
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.data.net.UserService
import io.reactivex.Flowable


class ApiDataSource(private val userService: UserService, private val userEntityMapper: UserEntityMapper) : UserDataSource {

    override fun getUserList() : Flowable<List<UserEntity>> =
        userService.getUsers()
            .map { it.results }
            .map {userEntityMapper.transformList(it)}

}