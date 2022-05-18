package com.example.photoclicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.databinding.FragmentEditAccountBinding
import com.example.photoclicker.model.entities.User
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditAccountFragment : Fragment() {

    private lateinit var binding: FragmentEditAccountBinding

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

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditAccountBinding.inflate(inflater, container,false)
        user = sharedViewModel.user.value!![0]

        setData()

        sharedViewModel.user.observe(this.viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                findNavController().navigate(R.id.action_editAccountFragment_to_loginFragment)
            }
            else setData()
        }

        binding.saveButton.setOnClickListener {
            updateUser()
        }

        binding.deleteAccountButton.setOnClickListener {
            deleteUser()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.GONE
        return binding.root
    }

    private fun deleteUser() {
        sharedViewModel.deleteUser(user)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.GONE
    }


    private fun updateUser(){
        val name = binding.nameText.text
        val surname = binding.surnameText.text
        val password = binding.passwordText.text

        if(!(name.isNullOrBlank() || password.isNullOrBlank())){
            sharedViewModel.updateUser(User(user.userId, user.login, password.toString(), name.toString(), surname.toString(), true))
            findNavController().navigateUp()
        }
        else Toast.makeText(context, "Please correct data!", Toast.LENGTH_SHORT).show()
    }

    private fun setData(){
        binding.loginText.text = user.login
        binding.nameText.setText(user.name)
        binding.surnameText.setText(user.surname)
        binding.passwordText.setText(user.password)
    }
}