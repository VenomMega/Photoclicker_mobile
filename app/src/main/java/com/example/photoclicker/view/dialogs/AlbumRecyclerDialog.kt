package com.example.photoclicker.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.adapters.AlbumListAdapter
import com.example.photoclicker.adapters.OnAlbumListener
import com.example.photoclicker.model.entities.Album
import com.example.photoclicker.model.entities.AlbumPictureCrossRef
import com.example.photoclicker.model.entities.Notification
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory

class AlbumRecyclerDialog: AppCompatDialogFragment(), OnAlbumListener{

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

    private lateinit var recyclerView: RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_recycler_album, null)

        recyclerView = view.findViewById(R.id.albums_recycler_view)

        val adapter = AlbumListAdapter(this)
        adapter.submitList(sharedViewModel.userAlbums.value!![0].albums)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        builder.setView(view)
            .setTitle("Choose album")
            .setNegativeButton("cancel", DialogInterface.OnClickListener {
                    dialogInterface, i ->
            } )


        return builder.create()
    }

    override fun onAlbumClick(album: Album, view: View) {
        sharedViewModel.addAlbumPictureCross(AlbumPictureCrossRef(album.albumId, sharedViewModel.pictureId))

        val message = sharedViewModel.user.value!![0].login.plus(" added your picture!")
        sharedViewModel.addNotification(Notification(0, sharedViewModel.authorId, message))
        findNavController().navigateUp()
    }
}