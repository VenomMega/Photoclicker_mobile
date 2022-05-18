package com.example.photoclicker.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.activityViewModels
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.model.entities.Album
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory

class AlbumDialog: AppCompatDialogFragment() {

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

    private lateinit var editTextName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_album, null)

        editTextName = view.findViewById(R.id.genre_name_edit_text)

        builder.setView(view)
            .setTitle("New album")
            .setNegativeButton("cancel", DialogInterface.OnClickListener {
                    dialogInterface, i ->

            } )
            .setPositiveButton("add", DialogInterface.OnClickListener {
                    dialogInterface, i ->
                val name = editTextName.text.toString()
                if(name.isNotBlank()){
                    sharedViewModel.addAlbum(Album(0, name,sharedViewModel.user.value!![0].userId))
                }
            } )

        return builder.create()
    }
}