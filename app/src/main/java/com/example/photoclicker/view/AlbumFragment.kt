package com.example.photoclicker.view

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.adapters.OnPictureListener
import com.example.photoclicker.adapters.PictureListAdapter
import com.example.photoclicker.databinding.FragmentAlbumBinding
import com.example.photoclicker.model.entities.AlbumPictureCrossRef
import com.example.photoclicker.model.entities.Picture
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class AlbumFragment : Fragment(), OnPictureListener, PopupMenu.OnMenuItemClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels{
        SharedViewModelFactory(
            (activity?.application as Application).database.albumDao(),
            (activity?.application as Application).database.albumPictureCrossRefDao(),
            (activity?.application as Application).database.notificationDao(),
            (activity?.application as Application).database.genreDao(),
            (activity?.application as Application).database.pictureDao(),
            (activity?.application as Application).database.userDao()
        )
    }

    private lateinit var binding: FragmentAlbumBinding
    private var userPictures = listOf<Picture>()
    private var deletePictureId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        sharedViewModel.setAlbumPictures()

        val adapter = PictureListAdapter(this)

        sharedViewModel.albumPictures.observe(this.viewLifecycleOwner){
            userPictures = it[0].pictures
            adapter.submitList(userPictures)
        }

        binding.userPicturesRecyclerView.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        binding.userPicturesRecyclerView.adapter = adapter

        setHasOptionsMenu(true)

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.GONE
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_fragment_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_photos_item -> {
                for(e in userPictures){
                    sharedViewModel.deleteAlbumPicture(AlbumPictureCrossRef(sharedViewModel.albumId, e.pictureId))
                }
            }
            R.id.delete_album_item -> {
                sharedViewModel.deleteAlbumWithId(sharedViewModel.albumId)
                sharedViewModel.albumId = 0
                findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPictureClick(picture: Picture, view: View) {
        deletePictureId = picture.pictureId
        val popup = PopupMenu(context, view)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.album_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return if (item != null) {
            sharedViewModel.deleteAlbumPicture(AlbumPictureCrossRef(sharedViewModel.albumId, deletePictureId))
            true
        } else false
    }
}