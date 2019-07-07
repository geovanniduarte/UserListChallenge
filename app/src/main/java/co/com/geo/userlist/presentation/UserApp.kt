package co.com.geo.userlist.presentation

import android.app.Application
import co.com.geo.userlist.di.components.ApplicationComponent
import co.com.geo.userlist.di.components.DaggerApplicationComponent
import co.com.geo.userlist.di.modules.ApplicationModule
import com.facebook.stetho.Stetho

class UserApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);

        // DI
        component =
                DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(this))
                    .build()
    }

}