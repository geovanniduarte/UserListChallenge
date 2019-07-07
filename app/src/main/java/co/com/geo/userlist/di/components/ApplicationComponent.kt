package co.com.geo.userlist.di.components

import co.com.geo.userlist.di.modules.ApplicationModule
import co.com.geo.userlist.di.modules.DataModule
import co.com.geo.userlist.di.modules.NetModule
import co.com.geo.userlist.presentation.userdetail.UserDetailActivity
import co.com.geo.userlist.presentation.userlist.UserListActivity
import co.com.geo.userlist.presentation.userlist.UserListFragment
import co.com.geo.userlist.util.mvvm.ViewModelModule
import dagger.Component

@Component(modules = [ApplicationModule::class, NetModule::class, DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(userListActivity: UserListActivity) //inject what UserListActivity needs
    fun inject(userListFragment: UserListFragment) //inject what UserListFragment needs
    fun inject(userDetailActivity: UserDetailActivity) //inject what UserDetailActivity needs
}