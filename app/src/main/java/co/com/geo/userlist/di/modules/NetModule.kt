package co.com.geo.userlist.di.modules

import co.com.geo.userlist.data.net.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetModule {

    @Provides
    fun provideRetrofit() : Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/")
            //.client(Inject.client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit
    }

    @Provides
    fun providesUserServie(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

}