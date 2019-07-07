package co.com.geo.userlist.presentation.userlist

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.com.geo.userlist.R
import co.com.geo.userlist.data.model.UserEntity
import co.com.geo.userlist.presentation.userdetail.UserDetailActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

typealias OnUserClick = (userEntity: UserEntity) -> Unit

class UserListAdapter(val onUserClick: OnUserClick): RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {
    override fun getItemCount(): Int = items.size

    private val items: MutableList<UserEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(items: List<UserEntity>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(userEntity: UserEntity) {
            with(itemView) {
                user_name.text = userEntity.username
                Glide.with(user_avatar)
                    .load(userEntity.thumbnail)
                    .into(user_avatar)
            }

            itemView.setOnClickListener{
                Log.i(UserListAdapter::class.java.canonicalName, "TEST GEO" + userEntity.hashCode())
                onUserClick(userEntity)
            }
        }
    }
}