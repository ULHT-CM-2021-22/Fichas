package com.github.mstavares.cm.fichas

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mstavares.cm.fichas.databinding.ItemPhotoBinding

class PhotoAdapter(
    private var items: List<PhotoUi> = listOf(),
    private val onClick: (PhotoUi) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val current = items[position]
        holder.binding.ivPhoto.setImageBitmap(BitmapFactory.decodeByteArray(current.photo, 0, current.photo.size))
        holder.itemView.setOnClickListener { onClick(current) }
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<PhotoUi>) {
        this.items = items
        notifyDataSetChanged()
    }

}