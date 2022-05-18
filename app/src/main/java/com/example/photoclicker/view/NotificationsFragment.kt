package com.example.photoclicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.adapters.NotificationListAdapter
import com.example.photoclicker.databinding.FragmentAlbumBinding
import com.example.photoclicker.databinding.FragmentNotificationsBinding
import com.example.photoclicker.model.entities.Notification
import com.example.photoclicker.model.entities.Picture
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationsFragment : Fragment() {

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

    private lateinit var binding: FragmentNotificationsBinding
    private var userNotifications = listOf<Notification>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        sharedViewModel.setUserNotifications()

        val adapter = NotificationListAdapter().apply {
            submitList(userNotifications)
        }

        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.notificationsRecyclerView.adapter = adapter

        sharedViewModel.userNotifications.observe(this.viewLifecycleOwner){
            userNotifications = it[0].notifications
            adapter.submitList(userNotifications)
        }

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)!!.visibility = View.VISIBLE
        return binding.root
    }
}