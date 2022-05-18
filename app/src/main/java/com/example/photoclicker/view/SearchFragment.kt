package com.example.photoclicker.view

import android.R.menu
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.adapters.*
import com.example.photoclicker.databinding.FragmentSearchBinding
import com.example.photoclicker.model.entities.Genre
import com.example.photoclicker.model.entities.Picture
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class SearchFragment : Fragment(), OnPictureListener, OnGenreListener {

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

    private lateinit var binding: FragmentSearchBinding
    private val adapter = SearchListAdapter(this)
    private val adapterGenre = GenreListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        if(sharedViewModel.pictures.value!!.toList().isNotEmpty()) adapter.setData(sharedViewModel.pictures.value!!.toList() as MutableList<Picture>)

        sharedViewModel.pictures.observe(this.viewLifecycleOwner){
            if(sharedViewModel.pictures.value!!.toList().isNotEmpty()) adapter.setData(it.toList() as MutableList<Picture>)
        }

        sharedViewModel.setAlbumPictures()

        sharedViewModel.genres.observe(this.viewLifecycleOwner){
            adapterGenre.submitList(it)
        }

        sharedViewModel.genrePictures.observe(this.viewLifecycleOwner){

        }

        binding.genreRecyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        binding.genreRecyclerView.adapter = adapterGenre

        binding.pictureRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.pictureRecyclerView.adapter = adapter

        setHasOptionsMenu(true)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.VISIBLE
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                if(newText.isNullOrBlank()) {
                    binding.genreRecyclerView.visibility = View.VISIBLE
                    binding.pictureRecyclerView.visibility = View.GONE
                }
                else{
                    binding.genreRecyclerView.visibility = View.GONE
                    binding.pictureRecyclerView.visibility = View.VISIBLE
                }
                return false
            }
        })

        return super.onOptionsItemSelected(item)
    }

    override fun onPictureClick(picture: Picture, view: View) {
        sharedViewModel.pictureId = picture.pictureId
        sharedViewModel.authorId = picture.authorId
        findNavController().navigate(R.id.action_searchFragment_to_photoFragment)
    }

    override fun onGenreClick(genre: Genre, view: View) {
        sharedViewModel.genreName = genre.genreName
        sharedViewModel.setGenrePictures()
        findNavController().navigate(R.id.action_searchFragment_to_genreFragment)
    }
}