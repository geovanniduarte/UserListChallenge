package co.com.geo.userlist.presentation.userlist

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import co.com.geo.userlist.R
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.presentation.UserApp
import co.com.geo.userlist.presentation.userdetail.SELECTED_USER_RESULT
import co.com.geo.userlist.presentation.userdetail.UserDetailActivity
import co.com.geo.userlist.util.FileUtils
import co.com.geo.userlist.util.Navigator
import co.com.geo.userlist.util.mvvm.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import javax.inject.Inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class UserListActivity : AppCompatActivity() {

    lateinit var userListViewModel: UserListViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var mSearchItem: MenuItem? = null
    var mToolbar: Toolbar? = null
    var mNewsAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initView()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun inject() {
        (application as UserApp).component.inject(this)
    }

    private fun init() {
        setUpViewModel()
    }

    private fun initView() {

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)


        if (supportFragmentManager.findFragmentById(R.id.all_users_fragment) == null) {
            val allUsersFragment = UserListFragment.newInstance(false)
            supportFragmentManager.beginTransaction()
                .add(R.id.all_users_fragment, allUsersFragment)
                .commit()
        }

        if (supportFragmentManager.findFragmentById(R.id.favorite_users_fragment) == null) {
            val allUsersFragment = UserListFragment.newInstance(true)
            supportFragmentManager.beginTransaction()
                .add(R.id.favorite_users_fragment, allUsersFragment)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        mSearchItem = menu?.findItem(R.id.m_search)

        setUpSearchBarAutoComplete(mSearchItem!!)

        mSearchItem?.let {
            it.setOnActionExpandListener( object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    // Called when SearchView is collapsing
                    if (it.isActionViewExpanded()) {
                        animateSearchToolbar(1, false, false)
                    }
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    // Called when SearchView is expanding
                    animateSearchToolbar(1, true, true)
                    return true
                }
            })
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_USER -> {
                if (resultCode == Activity.RESULT_OK) {
                    val receivedUser = data?.getParcelableExtra<UserEntity>(SELECTED_USER_RESULT)

                }
            }gi
        }
    }

    private fun setUpViewModel() {
        userListViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserListViewModel::class.java)
        bindEvents()
    }

    private fun bindEvents() {
        userListViewModel.userListByNameState.observe(this, Observer { userListByName ->
            userListByName?.let {
                changeSuggestions(it)
            }
        })
        userListViewModel.bindSearch()
    }

    private fun changeSuggestions(userListByName: List<String>) {
        mNewsAdapter?.run {
            //clear()
            addAll(userListByName)
            notifyDataSetChanged()
        }
    }

    fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {

        mToolbar?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        //mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(activity!!, R.color.quantum_grey_600))

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val width = mToolbar!!.getWidth() -
                        (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0) -
                        resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2
                val createCircularReveal = ViewAnimationUtils.createCircularReveal(
                    mToolbar,
                    if (isRtl(resources)) mToolbar!!.getWidth() - width else width,
                    mToolbar!!.getHeight() / 2,
                    0.0f,
                    width.toFloat()
                )
                createCircularReveal.duration = 250
                createCircularReveal.start()
            } else {
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, -mToolbar?.getHeight()!!.toFloat(), 0.0f)
                translateAnimation.duration = 220
                mToolbar?.clearAnimation()
                mToolbar?.startAnimation(translateAnimation)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val toolbarWidth = mToolbar?.width!!
                val width = toolbarWidth.minus(
                    (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0)
                ) - resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2


                val createCircularReveal = ViewAnimationUtils.createCircularReveal(
                    mToolbar,
                    if (isRtl(resources)) { mToolbar?.getWidth()?.minus(width)!! } else { width },
                    mToolbar?.getHeight()?.div(2)!!,
                    width.toFloat(),
                    0.0f
                )
                createCircularReveal.duration = 250
                createCircularReveal.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        mToolbar?.setBackgroundColor(getThemeColor(this as Context, R.attr.colorPrimary))
                    }
                })
                createCircularReveal.start()
            } else {
                val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, -mToolbar!!.getHeight() as Float)
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(alphaAnimation)
                animationSet.addAnimation(translateAnimation)
                animationSet.duration = 220
                animationSet.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        mToolbar?.setBackgroundColor(getThemeColor(this as Context, R.attr.colorPrimary))
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                mToolbar?.startAnimation(animationSet)
            }
            //mDrawerLayout.setStatusBarBackgroundColor(getThemeColor(activity., R.attr.colorPrimaryDark))
        }
    }

    private fun isRtl(resources: Resources): Boolean {
        return resources.getConfiguration().getLayoutDirection() === View.LAYOUT_DIRECTION_RTL
    }

    private fun getThemeColor(context: Context, id: Int): Int {
        val theme = context.getTheme()
        val a = theme.obtainStyledAttributes(intArrayOf(id))
        val result = a.getColor(0, 0)
        a.recycle()
        return result
    }

    private fun setUpSearchBarAutoComplete(searchMenu: MenuItem) {
        // Get SearchView object.
        var searchView = searchMenu.actionView as SearchView

        // Get SearchView autocomplete object.
        val searchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(android.support.v7.appcompat.R.id.search_src_text);

        // Create a new ArrayAdapter and add data to search auto complete object.
        val dataArr = arrayListOf("Apple")
        mNewsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dataArr)
        mNewsAdapter?.let {
            searchAutoComplete.setAdapter(it)
        }

        setUpQueryListener(searchAutoComplete, searchView)
    }

    private fun setUpQueryListener(searchAutoComplete: SearchView.SearchAutoComplete, searchView: SearchView) {
        // Listen to search view item on click event.
        searchAutoComplete.onItemClickListener = AdapterView.OnItemClickListener { adapterView: AdapterView<*>, view: View, itemIndex: Int, id: Long ->
            val queryString = adapterView.getItemAtPosition(itemIndex)
            searchAutoComplete.setText("" + queryString);
            Toast.makeText(this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
        }

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String) : Boolean {
                val alertDialog = AlertDialog.Builder(this as Context).create()
                alertDialog.setMessage("Search keyword is " + query)
                alertDialog.show()
                return false
            }

            override fun onQueryTextChange(newText: String) : Boolean {
                if (newText.length > 2) {
                    userListViewModel.query.value = newText //triggers user list by name search
                }
                return false
            }
        })
    }
}
