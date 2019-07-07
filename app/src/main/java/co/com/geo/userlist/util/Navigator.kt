package co.com.geo.userlist.util

import android.app.Activity
import android.content.Intent
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.presentation.userdetail.UserDetailActivity

class Navigator {
    fun openUserDetail(activity: Activity, userEntity: UserEntity, requestCode: Int) {
        val intent = UserDetailActivity.intent(activity, userEntity)
        activity.startActivityForResult(intent, requestCode)
    }
}