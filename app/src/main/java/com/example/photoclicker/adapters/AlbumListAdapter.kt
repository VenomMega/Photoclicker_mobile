package com.example.photoclicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photoclicker.R
import com.example.photoclicker.databinding.AlbumItemViewBinding
import com.example.photoclicker.model.entities.Album

class AlbumListAdapter(private val onAlbumListener: OnAlbumListener): ListAdapter<Album, AlbumListAdapter.AlbumHolder>(AlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_item_view, parent, false)
        return AlbumHolder(view, onAlbumListener)
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    class AlbumHolder(view: View, private val onAlbumListener: OnAlbumListener): RecyclerView.ViewHolder(view){
        private val binding = AlbumItemViewBinding.bind(view)
        private lateinit var item: Album

        fun bind(album: Album){
            item = album
            binding.nameText.text = item.name
            binding.albumItemView.setOnClickListener {
                onAlbumListener.onAlbumClick(item, binding.root)
            }
        }
    }
}

interface OnAlbumListener{
    fun onAlbumClick(album: Album, view: View)
}

class AlbumDiffCallback: DiffUtil.ItemCallback<Album>(){
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.albumId == newItem.albumId
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}