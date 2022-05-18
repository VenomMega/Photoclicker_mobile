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
import com.example.photoclicker.databinding.FragmentRegistrationBinding
import com.example.photoclicker.model.entities.User
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    private val sharedViewModel: SharedViewModel by activityViewModels(){
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
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        sharedViewModel.user.observe(this.viewLifecycleOwner){
            if(it.isNotEmpty()){
                findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
            }
        }

        binding.registerButton.setOnClickListener {
            if(checkFields()){
                sharedViewModel.registerUser(getUser())
            }
        }
        return binding.root
    }

    private fun checkFields(): Boolean{
        val login = binding.loginText.text.toString()
        val password = binding.passwordText.text.toString()
        val name = binding.nameText.text.toString()

        return if(login.isBlank() ||
            password.isBlank() ||
            name.isBlank()) {

            if (password.isBlank()) binding.passwordTextField.error = "Required!"
            if (login.isBlank()) binding.loginTextField.error = "Required!"
            if (name.isBlank()) binding.nameTextField.error = "Required!"

            false
        } else {
            if(!sharedViewModel.checkLogins(login)){
                binding.loginTextField.error = "Username is already taken!"
                return false
            }
            true
        }
    }

    private fun getUser(): User {
        return User(
            0,
            binding.loginText.text.toString(),
            binding.passwordText.text.toString(),
            binding.nameText.text.toString(),
            binding.surnameText.text.toString(),
            true
        )
    }
}