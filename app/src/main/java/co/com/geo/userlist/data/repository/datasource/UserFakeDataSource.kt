package co.com.geo.userlist.data.repository.datasource

import android.net.Uri
import co.com.geo.userlist.data.model.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import java.io.File

class UserFakeDataSource : UserDataSource {

    val userList = listOf(
        UserEntity( "","ismael","zeus", "geo@prueba.com", "555555", "https://randomuser.me/api/portraits/men/99.jpg", "https://randomuser.me/api/portraits/men/99.jpg", false),
        UserEntity( "","laura", "zeus", "geo@prueba.com", "555555", 	"https://randomuser.me/api/portraits/women/90.jpg", "https://randomuser.me/api/portraits/women/90.jpg", false),
        UserEntity( "","marc","zeus", "geo@prueba.com", "555555", "https://randomuser.me/api/portraits/men/38.jpg", "https://randomuser.me/api/portraits/men/38.jpg", false),
        UserEntity( "","frederik", "zeus","geo@prueba.com", "555555", 	"https://randomuser.me/api/portraits/men/12.jpg", "https://randomuser.me/api/portraits/men/12.jpg", false)

    )

    override fun getUserList(): Flowable<List<UserEntity>> =
        Flowable.just(userList)

}