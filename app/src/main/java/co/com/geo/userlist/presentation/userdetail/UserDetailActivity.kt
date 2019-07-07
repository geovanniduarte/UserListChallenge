package co.com.geo.userlist.presentation.userdetail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import co.com.geo.userlist.R
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.presentation.UserApp
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_detail.*
import javax.inject.Inject
import android.provider.ContactsContract



private const val STATE_IS_FAV = "is_fav"
const val SELECTED_USER_RESULT = "USER_RESULT"

class UserDetailActivity: AppCompatActivity() {


    companion object {
        const val PARAM_USER_ID = "user_id"

        fun intent(context: Context, userEntity: UserEntity) : Intent {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra(PARAM_USER_ID, userEntity)
            return intent
        }
    }

    var userEntity: UserEntity? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var userDetailViewModel: UserDetailViewModel
    var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        userEntity = intent.getParcelableExtra(PARAM_USER_ID)
        Log.i(UserDetailActivity::class.java.canonicalName, "TEST GEO" + userEntity.hashCode())
        init()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putBoolean(STATE_IS_FAV, isFavorite)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
         savedInstanceState?.run {
             isFavorite = getBoolean(STATE_IS_FAV)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val favoriteItem = menu?.findItem(R.id.detail_menu_fav)

        favoriteItem?.setOnMenuItemClickListener {
            toggleFavorite(it)
            true
        }

        applyFavorite(favoriteItem)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    private fun inject() {
        (application as UserApp).component.inject(this)
    }

    private fun init() {
        isFavorite = userEntity?.favorite!!
        setUpViewModel()
        initView()
    }

    private fun initView() {
        loadUserData()
        setUpButtons()
    }

    private fun setUpButtons() {
        add_user_button.setOnClickListener(View.OnClickListener {
            userEntity?.let {
                addAsContactConfirmed(it)
            }
        })
    }

    private fun setUpViewModel() {
        userDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailViewModel::class.java)
        bindEvents()
    }

    private fun bindEvents() {
        userDetailViewModel.userSaveState.observe(this, Observer {
            if (it != null) {
                if (it > 0) {
                    Toast.makeText(this, "Insert success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Insert failed", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Insert failed", Toast.LENGTH_LONG).show()
            }
        })

        userDetailViewModel.userState.observe(this, Observer { userEntity ->
            userEntity?.let {
                onUserLoaded(it)
            }
        })

        userDetailViewModel.loadingState.observe(this, Observer { isLoading ->
            isLoading?.let {
                showLoading(it)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        user_loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun loadUserData() {
        if (userEntity == null) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            userDetailViewModel.loadUserByIdDB(userEntity!!.id) //loads my own (db) user for update view data
        }
    }

    private fun onUserLoaded(userEntity: UserEntity) {
        user_detail_name.text = userEntity.name
        user_detail_email.text = userEntity.email
        user_detail_phone.text = userEntity.phone
        Glide.with(this)
            .load(userEntity.detailImg)
            .into(user_detail_avatar)
        isFavorite = userEntity.favorite
        invalidateOptionsMenu()
    }

    private fun toggleFavorite(menuItem: MenuItem) {
        if (!isFavorite) {
            menuItem.setIcon(R.drawable.ic_baseline_favorite_24px)
        } else {
            menuItem.setIcon(R.drawable.ic_baseline_favorite_border_24px)
        }
        isFavorite = !isFavorite
        userEntity?.favorite = isFavorite
        insertEditUser()
    }

    private fun applyFavorite(menuItem: MenuItem?) {
        menuItem?.run {
            if (isFavorite) {
                setIcon(R.drawable.ic_baseline_favorite_24px)
            } else {
                setIcon(R.drawable.ic_baseline_favorite_border_24px)
            }
        }
    }

    private fun insertEditUser() {
        userEntity?.let {
            userDetailViewModel.insertEditOne(it)
        }
    }

    /**
     * Open the add-contact screen with pre-filled info
     *
     * @param context
     * Activity context
     * @param person
     * [Person] to add to contacts list
     */
    fun addAsContactConfirmed(userEntity: UserEntity) {

        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = ContactsContract.Contacts.CONTENT_TYPE

        intent.putExtra(ContactsContract.Intents.Insert.NAME, userEntity.name)
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, userEntity.phone)
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, userEntity.email)

        startActivity(intent)

    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().apply {
            this.putExtra(SELECTED_USER_RESULT, userEntity)
        })
        super.finish()
    }
}