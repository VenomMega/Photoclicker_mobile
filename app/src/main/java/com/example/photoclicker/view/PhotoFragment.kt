package com.example.photoclicker.view

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.databinding.FragmentPhotoBinding
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class PhotoFragment : Fragment() {

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

    private lateinit var binding: FragmentPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        setData()

        sharedViewModel.setUserAlbums()
        sharedViewModel.userAlbums.observe(this.viewLifecycleOwner) {
        }

        binding.saveButton.setOnClickListener {
            if (sharedViewModel.userAlbums.value.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_photoFragment_to_albumDialog)
            } else {
                findNavController().navigate(R.id.action_photoFragment_to_albumRecyclerDialog)
            }
        }

        binding.downloadButton.setOnClickListener {
            saveToGallery()
        }

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.GONE
        return binding.root
    }

    private fun setData() {
        val picture = sharedViewModel.getPicture()
        if (picture != null) {
            binding.photoImageView.setImageBitmap(picture.photo)
            binding.pictureNameText.text = picture.name

            val by = "By: "
            binding.pictureAuthorText.text = by.plus(picture.authorName)
                ///by.plus(sharedViewModel.findAuthor(picture.authorId))
        }
    }

    private fun saveToGallery() {
        val bitmap = sharedViewModel.getPicture()!!.photo
        val savedImageURL = MediaStore.Images.Media.insertImage(
            requireActivity().contentResolver,
            bitmap,
            requireActivity().toString(),
            "Image of ${requireActivity().title}"
        )

        Uri.parse(savedImageURL)
        Toast.makeText(context, "Picture is downloading!", Toast.LENGTH_SHORT).show()
    }
}