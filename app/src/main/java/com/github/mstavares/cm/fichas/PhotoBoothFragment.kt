package com.github.mstavares.cm.fichas

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.mstavares.cm.fichas.databinding.FragmentPhotoBoothBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class PhotoBoothFragment : Fragment() {

    private val TAG = PhotoBoothFragment::class.java.simpleName
    private val TAKE_PHOTO_CODE = 1000

    private lateinit var binding: FragmentPhotoBoothBinding
    private lateinit var viewModel: PhotoBoothViewModel
    private var adapter = PhotoAdapter(onClick = ::onPhotoClick)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.photo_booth)
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
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if(requestCode == TAKE_PHOTO_CODE && intent != null) {
            val bitmap = intent.extras?.get("data") as Bitmap
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

}