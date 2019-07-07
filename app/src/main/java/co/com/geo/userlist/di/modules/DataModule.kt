package co.com.geo.userlist.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import co.com.geo.userlist.data.db.UserDatabase
import co.com.geo.userlist.data.mappers.UserEntityMapper
import co.com.geo.userlist.data.net.UserService
import co.com.geo.userlist.data.repository.UserRepository
import co.com.geo.userlist.data.repository.datasource.ApiDataSource
import co.com.geo.userlist.data.repository.datasource.LocalDatasource
import co.com.geo.userlist.util.SettingsManager
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideUserEntityMapper()  = UserEntityMapper()

    @Provides
    fun provideSharedPreferences(context: Context) : SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideSettingsManager(sharedPreferences: SharedPreferences) : SettingsManager =
        SettingsManager(sharedPreferences)

    @Provides
    fun provideDatabase(context: Context): UserDatabase =
        Room.databaseBuilder(context, UserDatabase::class.java, "user.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideLocalDataSource(userDatabase: UserDatabase): LocalDatasource =
        LocalDatasource(userDatabase)

    @Provides
    fun provideApiDataSource(userService: UserService, userEntityMapper: UserEntityMapper) : ApiDataSource =
            ApiDataSource(userService, userEntityMapper)

    @Provides
    fun provideRepository(localDatasource: LocalDatasource, apiDataSource: ApiDataSource) : UserRepository =
            UserRepository(localDatasource, apiDataSource)
}