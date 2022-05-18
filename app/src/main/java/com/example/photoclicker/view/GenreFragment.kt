package com.example.photoclicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.adapters.OnPictureListener
import com.example.photoclicker.adapters.PictureListAdapter
import com.example.photoclicker.databinding.FragmentGenreBinding
import com.example.photoclicker.databinding.FragmentPhotoBinding
import com.example.photoclicker.model.entities.Picture
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class GenreFragment : Fragment(), OnPictureListener {

    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(
            (activity?.application as Application).database.albumDao(),
            (activity?.application as Application).database.albumPictureCrossRefDao(),
            (activity?.application as Application).database.notificationDao(),
            (activity?.application as Application).database.genreDao(),
            (activity?.application as Application).database.pictureDao(),
            (activity?.application as Application).database.userDao()
        )
    }

    private lateinit var binding: FragmentGenreBinding
    private var pictures = listOf<Picture>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenreBinding.inflate(inflater, container, false)

        binding.genreNameText.text = sharedViewModel.genreName

        val adapter = PictureListAdapter(this).apply {
            submitList(pictures)
        }

        sharedViewModel.genrePictures.observe(this.viewLifecycleOwner){
            pictures = it[0].pictures
            adapter.submitList(pictures)
        }

        binding.genreRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.genreRecyclerView.adapter = adapter

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.GONE
        return binding.root
    }

    override fun onPictureClick(picture: Picture, view: View) {
        sharedViewModel.pictureId = picture.pictureId
        sharedViewModel.authorId = picture.authorId
        findNavController().navigate(R.id.action_genreFragment_to_photoFragment)
    }
}