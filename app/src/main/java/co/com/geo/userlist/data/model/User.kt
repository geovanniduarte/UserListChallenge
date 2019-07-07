package co.com.geo.userlist.data.model

import android.support.v7.widget.DialogTitle

data class User(
    val login: Login,
    val name: Name,
    val email: String,
    val phone: String,
    val picture: Picture
) {

}

data class Login(
    val uuid:String,
    val username: String
)

data class Name(
    val title: String,
    val first: String,
    val last: String
)

data class Picture(val large: String,
                   val medium: String,
                   val thumbnail: String)