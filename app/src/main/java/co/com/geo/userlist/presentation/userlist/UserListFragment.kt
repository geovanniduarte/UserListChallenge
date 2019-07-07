package co.com.geo.userlist.presentation.userlist


import android.animation.Animator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import co.com.geo.userlist.R
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.presentation.UserApp
import co.com.geo.userlist.util.Navigator
import kotlinx.android.synthetic.main.fragment_user_list.*
import javax.inject.Inject
import android.support.v4.view.MenuItemCompat.isActionViewExpanded
import android.support.v4.view.MenuItemCompat
import android.content.res.TypedArray
import android.content.res.Resources.Theme
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.opengl.ETC1.getHeight
import android.view.animation.TranslateAnimation
import android.view.animation.AlphaAnimation
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Resources
import android.opengl.ETC1.getWidth
import android.view.ViewAnimationUtils
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.*
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_IS_FAV = "is_fav"
private const val ARG_GRID_COLUMS = "grid_columns"

const val REQUEST_USER = 1
/**
 * A simple [Fragment] subclass.
 * Use the [UserListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class UserListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var isFavorite: Boolean? = null
    private var gridColumns: Int? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isFavorite wheter the list is for the favorite users.
         * @return A new instance of fragment UserListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(isFavorite: Boolean) =
            UserListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_FAV, isFavorite)
                }
            }
    }

    lateinit var userListViewModel: UserListViewModel

    @Inject
    lateinit var navigator : Navigator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var loading = false

    private val adapter = UserListAdapter {
        onUserCliked(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    private fun inject() {
        activity?.let {fragmentActivity ->
            fragmentActivity.application?.let { app ->
                (app as UserApp).component.inject(this)
            }
        }
    }

    private fun init() {
        setUpViewModel()
    }

    private fun initView() {
        setHasOptionsMenu(true)

        arguments?.let {
            isFavorite = it.getBoolean(ARG_IS_FAV)
        }

        isFavorite?.let {
            if (it) {
                gridColumns = 1
                userListViewModel.loadUserListFavorite()
            } else {
                gridColumns = 2
                userListViewModel.loadUserList() //load  users to ui
            }
        }
        setUpRecycler()

    }

    private fun setUpRecycler() {
        user_list_recycler.layoutManager = GridLayoutManager(this.activity, gridColumns!!)
        user_list_recycler.itemAnimator = DefaultItemAnimator()
        user_list_recycler.adapter = adapter

        user_list_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
                val lastVisiblePos = gridLayoutManager!!.findLastVisibleItemPosition()
                if (!loading && gridLayoutManager!!.itemCount <= lastVisiblePos + 2) {
                    loading = true
                    if (!isFavorite!!) {
                        userListViewModel.loadUserList()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun setUpViewModel() {
        userListViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserListViewModel::class.java)
        bindEvents()
    }

    private fun bindEvents() {
        userListViewModel.isLoadingState.observe(this, Observer { isLoading ->
            isLoading?.let {
                showLoading(it)
            }
        })
        userListViewModel.userListState.observe(this, Observer { userList ->
            userList?.let {
                onUserListLoaded(it)
            }
        })
        userListViewModel.userListFavoriteState.observe(this, Observer { userList ->
            userList?.let {
                onUserListLoaded(it)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        user_list_loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun onUserListLoaded(userList: List<UserEntity>) {
        adapter.submitList(userList)
        loading = false
    }

    private fun onUserCliked(userEntity: UserEntity) {
        if (!isFavorite!!) {
            navigator.openUserDetail(this.activity!!, userEntity, REQUEST_USER)
        }
    }

    fun submmitNewUserEntity(userEntity: UserEntity) {
        val listOnUser = mutableListOf(userEntity)
        onUserListLoaded(listOnUser)
    }
}
