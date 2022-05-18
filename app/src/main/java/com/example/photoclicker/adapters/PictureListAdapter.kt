package com.example.photoclicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photoclicker.R
import com.example.photoclicker.databinding.PictureItemViewBinding
import com.example.photoclicker.model.entities.Picture


class PictureListAdapter(private val onPictureListener: OnPictureListener): ListAdapter<Picture, PictureListAdapter.PictureHolder>(PictureDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.picture_item_view, parent, false)
        return PictureHolder(view, onPictureListener)
    }

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    class PictureHolder(view: View, private val onPictureListener: OnPictureListener): RecyclerView.ViewHolder(view){

        private val binding = PictureItemViewBinding.bind(view)
        private lateinit var item: Picture

        fun bind(picture: Picture){
            item = picture
            binding.pictureCardView.setImageBitmap(item.photo)
            setListeners()
        }

        private fun setListeners(){
            binding.pictureCardView.setOnClickListener{
                onPictureListener.onPictureClick(item, binding.pictureCardView)
            }
        }
    }
}

interface OnPictureListener{
    fun onPictureClick(picture: Picture, view: View)
}

class PictureDiffCallback: DiffUtil.ItemCallback<Picture>(){
    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.pictureId == newItem.pictureId
    }

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem == newItem
    }
}