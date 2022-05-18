package com.example.photoclicker.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.photoclicker.R
import com.example.photoclicker.databinding.PictureItemViewBinding
import com.example.photoclicker.model.entities.Picture
import java.util.*

class SearchListAdapter(private val onPictureListener: OnPictureListener) :
    RecyclerView.Adapter<SearchListAdapter.SearchHolder>(), Filterable {

    var pictures = mutableListOf<Picture>()
    lateinit var picturesAll: List<Picture>

    @SuppressLint("NotifyDataSetChanged")
    fun setData(pictures: MutableList<Picture>) {
        this.pictures = pictures
        picturesAll = this.pictures.toList()
        this.notifyDataSetChanged()
    }

    class SearchHolder(itemView: View, private val onPictureListener: OnPictureListener) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = PictureItemViewBinding.bind(itemView)

        fun onBind(item: Picture) {
            binding.pictureCardView.setImageBitmap(item.photo)
            binding.root.setOnClickListener {
                onPictureListener.onPictureClick(item, binding.root)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.picture_item_view, parent, false)
        return SearchHolder(view, onPictureListener)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.onBind(pictures[position])
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {

                val filteredList = mutableListOf<Picture>()

                if (p0 == null || p0.isEmpty()) {
                    filteredList.addAll(0, picturesAll)
                } else {
                    for (picture in picturesAll) {
                        if (picture.name.lowercase(Locale.getDefault())
                                .contains(p0.toString().lowercase(Locale.getDefault())) ||
                            picture.genreName.lowercase(Locale.getDefault())
                                .contains(p0.toString().lowercase(Locale.getDefault())) ||
                            picture.authorName.lowercase(Locale.getDefault())
                                .contains(p0.toString().lowercase(Locale.getDefault()))
                        ) {
                            filteredList.add(picture)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (p1 != null) {
                    val result = p1.values as List<Picture>
                    pictures = result as MutableList<Picture>
                    notifyDataSetChanged()
                }
            }
        }
    }
}