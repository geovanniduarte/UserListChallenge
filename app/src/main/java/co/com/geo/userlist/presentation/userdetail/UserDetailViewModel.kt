package co.com.geo.userlist.presentation.userdetail

import android.arch.lifecycle.MutableLiveData
import co.com.geo.userlist.data.model.User
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.data.repository.UserRepository
import co.com.geo.userlist.util.mvvm.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(private val userRepository: UserRepository): BaseViewModel() {

    val userSaveState : MutableLiveData<Long> = MutableLiveData()
    val userState : MutableLiveData<UserEntity> = MutableLiveData()
    val loadingState : MutableLiveData<Boolean> = MutableLiveData()

    fun insertEditOne(userEntity: UserEntity) =
        userRepository.insertEditOne(userEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                userSaveState.value = it
            }
            .addTo(compositeDisposable)

    fun loadUserByIdDB(uuid: String) =
            userRepository.loadOneDB(uuid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete {
                    loadingState.postValue(false)
                }
                .doOnSubscribe {
                    loadingState.postValue(true)
                }
                .subscribe {
                    userState.value = it
                }
}