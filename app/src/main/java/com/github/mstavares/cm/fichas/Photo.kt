package com.github.mstavares.cm.fichas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.codec.binary.Base64

class Photo(private val dao: PhotoDao) {

    // Base64.getEncoder() precisa de verificar a versão do Android, não queremos imports Android aqui!
    fun insertPhoto(bytes: ByteArray, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val photoEncoded = Base64().encode(bytes).toString(Charsets.UTF_8)
            dao.insert(PhotoRoom(photoEncoded = photoEncoded))
            onFinished()
        }
    }

    fun getAllPhotos(onFinished: (List<PhotoUi>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val photos = dao.getAll()
            val base64 = Base64()
            onFinished(photos.map { PhotoUi(it.id, base64.decode(it.photoEncoded.toByteArray()))})
        }
    }

}