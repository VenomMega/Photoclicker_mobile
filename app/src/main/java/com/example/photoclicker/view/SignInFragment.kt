package com.example.photoclicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.databinding.FragmentLoginBinding
import com.example.photoclicker.databinding.FragmentSignInBinding
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import kotlin.math.log

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        sharedViewModel.user.observe(this.viewLifecycleOwner){
            if(it.isNotEmpty()){
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }
        }

        binding.signInButton.setOnClickListener {
            if(checkFields()){
                checkUser()
            }
        }

        return binding.root
    }

    private fun checkFields(): Boolean{
        val login = binding.loginText.text.toString()
        val password = binding.passwordText.text.toString()

        return if(login.isBlank() ||
            password.isBlank()) {

            if (password.isBlank()) binding.passwordTextField.error = "Required!"
            if (login.isBlank()) binding.loginTextField.error = "Required!"

            false
        } else true
    }

    private fun checkUser(){
        val login = binding.loginText.text.toString()
        val password = binding.passwordText.text.toString()

        if(!sharedViewModel.checkUser(login, password)){
            binding.loginTextField.error = "Account does not exist!"
        }
    }
}