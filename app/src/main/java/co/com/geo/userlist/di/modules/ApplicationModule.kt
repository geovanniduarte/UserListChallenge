package co.com.geo.userlist.di.modules

import android.app.Application
import android.content.Context
import co.com.geo.userlist.util.Navigator
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(val application: Application) {

    @Provides
    fun provideContext() : Context = application.applicationContext

    @Provides
    fun privideNavigator() : Navigator = Navigator()
}