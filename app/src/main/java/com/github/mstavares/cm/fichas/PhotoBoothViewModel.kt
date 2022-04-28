package com.github.mstavares.cm.fichas

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class PhotoBoothViewModel(application: Application) : AndroidViewModel(application) {

    private val model = Photo(CalculatorDatabase.getInstance(application).photoDao())

    fun onSavePhoto(bytes: ByteArray, onFinished: () -> Unit) {
        model.insertPhoto(bytes, onFinished)
    }

    fun onGetAllPhotos(onFinished: (List<PhotoUi>) -> Unit) {
        model.getAllPhotos(onFinished)
    }

}