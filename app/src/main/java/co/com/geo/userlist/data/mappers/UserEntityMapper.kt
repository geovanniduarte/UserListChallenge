package co.com.geo.userlist.data.mappers

import co.com.geo.userlist.data.model.User
import co.com.geo.userlist.data.model.UserEntity

class UserEntityMapper : Mapper<User, UserEntity>{
    override fun transform(input: User): UserEntity = UserEntity(input.login.uuid,
        "${input.name.title} ${input.name.first} ${input.name.last}",
        input.login.username,
        input.email,
        input.phone,
        input.picture.thumbnail,
        input.picture.large, false)


    override fun transformList(inputList: List<User>): List<UserEntity> = inputList.map { transform(it) }

}