package co.com.geo.userlist.util

import android.content.SharedPreferences

class SettingsManager(val sharedPreferences: SharedPreferences) {

    companion object {
        const val PREF_FIRST_LOAD = "first_load"
    }

    var fistLoad : Boolean
    get() = sharedPreferences.getBoolean(PREF_FIRST_LOAD, false)
    set(value) {
        sharedPreferences.edit()
            .putBoolean(PREF_FIRST_LOAD, value)
            .apply()
    }

}