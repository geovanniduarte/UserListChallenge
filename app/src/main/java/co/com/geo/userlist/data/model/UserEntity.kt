package co.com.geo.userlist.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "users")
@Parcelize
data class UserEntity (
    @PrimaryKey
    val id:String,
    val name: String,
    val username: String,
    val email:String,
    val phone:String,
    @ColumnInfo(name="thumbnail")
    val thumbnail: String,
    @ColumnInfo(name="detail_img")
    val detailImg:String,
    var favorite: Boolean

) : Parcelable