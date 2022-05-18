package com.example.photoclicker.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.adapters.AlbumListAdapter
import com.example.photoclicker.adapters.OnAlbumListener
import com.example.photoclicker.adapters.PictureListAdapter
import com.example.photoclicker.databinding.FragmentAccountBinding
import com.example.photoclicker.model.entities.Album
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountFragment : Fragment(), OnAlbumListener {

    private lateinit var binding: FragmentAccountBinding

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

    private var userAlbums = listOf<Album>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        sharedViewModel.user.observe(this.viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
            }
            else setData()
        }

        sharedViewModel.setUserAlbums()

        val adapter = AlbumListAdapter(this)

        sharedViewModel.userAlbums.observe(this.viewLifecycleOwner){
            userAlbums = it[0].albums
            adapter.submitList(userAlbums)
        }

        binding.albumsRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.albumsRecyclerView.adapter = adapter

        setHasOptionsMenu(true)

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.VISIBLE
        return binding.root
    }

    private fun setData(){
        binding.loginText.text = sharedViewModel.user.value!![0].login
        binding.nameText.text = sharedViewModel.user.value!![0].name.plus(" ").plus(sharedViewModel.user.value!![0].surname)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_fragment_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_picture_item -> findNavController().navigate(R.id.action_accountFragment_to_addPictureFragment)
            R.id.add_album_item -> findNavController().navigate(R.id.action_accountFragment_to_albumDialog)
            R.id.edit_account_item -> findNavController().navigate(R.id.action_accountFragment_to_editAccountFragment)
            R.id.exit_item -> {
                activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.GONE
                sharedViewModel.exitUser(sharedViewModel.user.value!![0])
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAlbumClick(album: Album, view: View) {
        sharedViewModel.albumId = album.albumId
        findNavController().navigate(R.id.action_accountFragment_to_albumFragment)
    }
}