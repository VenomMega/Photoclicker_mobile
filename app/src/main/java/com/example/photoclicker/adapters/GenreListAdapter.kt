package com.example.photoclicker.adapters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photoclicker.R
import com.example.photoclicker.databinding.AlbumItemViewBinding
import com.example.photoclicker.databinding.GenreItemViewBinding
import com.example.photoclicker.model.entities.Album
import com.example.photoclicker.model.entities.Genre

class GenreListAdapter(private val onGenreListener: OnGenreListener,
                       ): ListAdapter<Genre, GenreListAdapter.GenreHolder>(GenreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.genre_item_view, parent, false)
        return GenreHolder(view, onGenreListener)
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    class GenreHolder(view: View, private val onGenreListener: OnGenreListener): RecyclerView.ViewHolder(view){

        private val binding = GenreItemViewBinding.bind(view)
        private lateinit var item: Genre

        fun bind(genre: Genre){
            item = genre
            binding.nameText.text = item.genreName
            binding.genreLayoutView.setImageBitmap(item.genreImg)

            binding.genreLayoutView.setOnClickListener {
                onGenreListener.onGenreClick(item, binding.root)
            }
        }
    }
}

interface OnGenreListener{
    fun onGenreClick(genre: Genre, view: View)
}

class GenreDiffCallback: DiffUtil.ItemCallback<Genre>(){
    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.genreName == newItem.genreName
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }
}