package co.com.geo.userlist.data.model

data class UserApiResponse(
    val results: List<User>
)

data class UploadVideoResponse(
    val success: Boolean,
    val id: String?
)

data class UserEntitySeven(
    val Id: String,
    val Email: String,
    val NombresApellidos: String
) {

}

data class UserApiResponseSeven (
    val results: List<UserEntitySeven>
)