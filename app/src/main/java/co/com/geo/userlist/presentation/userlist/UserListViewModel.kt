package co.com.geo.userlist.presentation.userlist

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.data.repository.UserRepository
import co.com.geo.userlist.util.SettingsManager
import co.com.geo.userlist.util.mvvm.BaseViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val settingsManager: SettingsManager
) : BaseViewModel() {

    val userListState: MutableLiveData<List<UserEntity>> = MutableLiveData()
    val isLoadingState: MutableLiveData<Boolean> = MutableLiveData()
    val userListByNameState: MutableLiveData<List<String>> = MutableLiveData()
    var userListFavoriteState: MutableLiveData<List<UserEntity>> = MutableLiveData()

    //DATA
    var userList: List<UserEntity>? = null


    fun loadUserList() {
        userRepository.getUserList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isLoadingState.postValue(true)
            }
            .doOnError {
                it.printStackTrace()
            }
            .doOnTerminate {
                isLoadingState.postValue(false)
            }
            .subscribeBy(
                onNext = {
                    userListState.value = it
                    userList = it
                },
                onError = {
                    it.printStackTrace()
                },
                onComplete = {
                    settingsManager.fistLoad = false
                }
            ).addTo(compositeDisposable)

    }

    fun loadUserListFavorite() =
        userRepository.
            getAllUserFavorite()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    userListFavoriteState.value = it
                },
                onError = {

                }
            )

    val query = Variable("")

    val searchResult = query.observable .toFlowable(BackpressureStrategy.BUFFER)
        .distinctUntilChanged()
        .debounce(3, TimeUnit.SECONDS)
        .flatMap {
            userRepository.getAllUsersBy("%$it%")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        }


    fun bindSearch() = searchResult
        .subscribeBy(
            onNext = {
                userListByNameState.value = it
            },
            onError = {
                it.printStackTrace()
            },
            onComplete = {
                Log.i("complete", "compleado")
            }
        ).addTo(compositeDisposable)
}

class Variable<T>(private val defaultValue: T) {
    var value: T = defaultValue
        set(value) {
            field = value
            observable.onNext(value)
        }
    val observable = BehaviorSubject.createDefault(value)
}