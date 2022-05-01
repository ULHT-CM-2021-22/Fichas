package com.github.mstavares.cm.fichas

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.mstavares.cm.fichas.databinding.FragmentPhotoBoothBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class PhotoboothFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBoothBinding
    private lateinit var viewModel: PhotoBoothViewModel
    private var adapter = PhotoAdapter(onClick = ::onPhotoClick)

    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            viewModel.onSavePhoto(baos.toByteArray()) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, getString(R.string.photo_saved),Toast.LENGTH_LONG).show()
                    viewModel.onGetAllPhotos { updatePhotos(it) }
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.photobooth)
        val view = inflater.inflate(R.layout.fragment_photo_booth, container, false)
        viewModel = ViewModelProvider(this).get(PhotoBoothViewModel::class.java)
        binding = FragmentPhotoBoothBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.rvPhotoGallery.adapter = adapter
        binding.fabCamera.setOnClickListener { takePhoto() }
        viewModel.onGetAllPhotos { updatePhotos(it) }
    }

    private fun takePhoto() {
        permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
            if (result.allGranted()) {
                cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            } else {
                Toast.makeText(context, getString(R.string.no_camera_permission), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onPhotoClick(selected: PhotoUi) {
        binding.ivPhoto.setImageBitmap(BitmapFactory.decodeByteArray(selected.photo, 0, selected.photo.size))
        binding.ivPhoto.visibility = View.VISIBLE
        binding.tvPhotoMessage.visibility = View.GONE
    }

    private fun updatePhotos(photos: List<PhotoUi>) {
        CoroutineScope(Dispatchers.Main).launch {
            adapter.updateItems(photos)
        }
    }

}
