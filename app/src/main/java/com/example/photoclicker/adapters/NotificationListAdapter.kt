package com.example.photoclicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photoclicker.R
import com.example.photoclicker.databinding.AlbumItemViewBinding
import com.example.photoclicker.databinding.NotificationItemViewBinding
import com.example.photoclicker.model.entities.Album
import com.example.photoclicker.model.entities.Notification

class NotificationListAdapter(): ListAdapter<Notification, NotificationListAdapter.NotificationHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item_view, parent, false)
        return NotificationHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    class NotificationHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = NotificationItemViewBinding.bind(view)
        private lateinit var item: Notification

        fun bind(notification: Notification){
            item = notification
            binding.nameText.text = item.message
        }
    }
}

class NotificationDiffCallback: DiffUtil.ItemCallback<Notification>(){
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }
}