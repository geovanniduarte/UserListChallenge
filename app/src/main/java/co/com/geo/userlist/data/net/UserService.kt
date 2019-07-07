package co.com.geo.userlist.data.net

import co.com.geo.userlist.data.model.UploadVideoResponse
import co.com.geo.userlist.data.model.UserApiResponse
import co.com.geo.userlist.data.model.UserApiResponseSeven
import co.com.geo.userlist.data.model.UserEntitySeven
import io.reactivex.Completable
import io.reactivex.Flowable
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @GET("?results=20")
    fun getUsers(): Flowable<UserApiResponse>
}