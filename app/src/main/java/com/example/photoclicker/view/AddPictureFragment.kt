package com.example.photoclicker.view

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.databinding.FragmentAddPictureBinding
import com.example.photoclicker.model.entities.Genre
import com.example.photoclicker.model.entities.Picture
import com.example.photoclicker.view.dialogs.GenreDialog
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddPictureFragment : Fragment() {

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

    private lateinit var binding: FragmentAddPictureBinding

    private val genres = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPictureBinding.inflate(inflater, container, false)

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, genres)
        binding.genreSpinner.adapter = spinnerAdapter

        sharedViewModel.genres.observe(this.viewLifecycleOwner){
            genres.clear()
            for(genre in it){
                genres.add(genre.genreName)
            }
            spinnerAdapter.notifyDataSetChanged()
        }

        binding.addGenre.setOnClickListener {
            findNavController().navigate(R.id.action_addPictureFragment_to_genreDialog)
        }

        binding.addButton.setOnClickListener {
            checkData()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.chooseImgButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 3)
        }

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.GONE
        return binding.root
    }

    private fun checkData() {
        val pictureImg = binding.photoImageView.drawable
        val pictureName = binding.nameText.text.toString()
        val genreName = binding.genreSpinner.selectedItem

        if(pictureImg==null){
            Toast.makeText(context, "Image has not been selected!", Toast.LENGTH_SHORT).show()
        }
        else if(pictureName.isBlank()){
            Toast.makeText(context, "Name can not be empty!", Toast.LENGTH_SHORT).show()
        }
        else if(genreName is String && genreName.isBlank()){
            Toast.makeText(context, "Choose genre!", Toast.LENGTH_SHORT).show()
        }
        else if(genreName==null){
            Toast.makeText(context, "Add genre!", Toast.LENGTH_SHORT).show()
        }
        else{
            val authorName = sharedViewModel.user.value!![0].login
            sharedViewModel.addPicture(Picture(0,pictureName, genreName.toString(), sharedViewModel.user.value!![0].userId, authorName, pictureImg.toBitmap()))
            findNavController().navigateUp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && data != null){
            val selectedImage = data.data
            binding.photoImageView.setImageURI(selectedImage)
        }
    }
}