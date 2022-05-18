package com.example.photoclicker.view.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import com.example.photoclicker.Application
import com.example.photoclicker.R
import com.example.photoclicker.model.entities.Genre
import com.example.photoclicker.viewmodel.SharedViewModel
import com.example.photoclicker.viewmodel.SharedViewModelFactory

class GenreDialog: AppCompatDialogFragment() {

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
    private lateinit var imageView: ImageView
    private lateinit var button: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_genre, null)

        editTextName = view.findViewById(R.id.genre_name_edit_text)
        imageView = view.findViewById(R.id.genre_img_view)
        button = view.findViewById(R.id.add_genre_img_button)

        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 3)
        }

        builder.setView(view)
            .setTitle("New genre")
            .setNegativeButton("cancel", DialogInterface.OnClickListener {
                    dialogInterface, i ->

            } )
            .setPositiveButton("add", DialogInterface.OnClickListener {
                    dialogInterface, i ->
                val name = editTextName.text.toString()

                if(name.isNotBlank()){
                    sharedViewModel.addGenre(Genre(name, imageView.drawable.toBitmap()))
                }
            } )

        return builder.create()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && data != null){
            val selectedImage = data.data
            imageView.setImageURI(selectedImage)
        }
    }
}