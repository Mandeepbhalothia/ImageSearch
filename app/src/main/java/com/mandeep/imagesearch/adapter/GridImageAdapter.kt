package com.mandeep.imagesearch.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import com.mandeep.imagesearch.model.Photo
import com.mandeep.imagesearch.utilities.Common.hideProgressBar
import com.mandeep.imagesearch.utilities.SquareImage
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import kotlinx.android.synthetic.main.grid_image_item.view.*

class GridImageAdapter(context: Context, var layout: Int, photoList: ArrayList<Photo>) :
    ArrayAdapter<Photo>(context, layout, photoList) {

    private var photoList: ArrayList<Photo>
    private var layoutInflater: LayoutInflater

    init {
        this.photoList = photoList
        this.layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun updatePhotoList(photoList: ArrayList<Photo>) {
        this.photoList = photoList
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: PhotoViewHolder
        val retView: View
        if (convertView == null) {
            retView = layoutInflater.inflate(layout, null)
            viewHolder = PhotoViewHolder()
            viewHolder.gridImageImageView = retView.gridImageImageView
            viewHolder.gridImageProgressBar = retView.gridImageProgressBar

            retView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as PhotoViewHolder
            retView = convertView
        }


        if ((position > 0) and (position < photoList.size)) {
            val photo = photoList[position]
            val imageLoader = ImageLoader.getInstance()
            imageLoader.displayImage(photo.photoUrl, viewHolder.gridImageImageView, object :
                ImageLoadingListener {
                override fun onLoadingComplete(
                    imageUri: String?,
                    view: View?,
                    loadedImage: Bitmap?
                ) {
                    viewHolder.gridImageProgressBar?.let { hideProgressBar(it) }
                }

                override fun onLoadingStarted(imageUri: String?, view: View?) {
                    viewHolder.gridImageProgressBar?.let { hideProgressBar(it) }
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    viewHolder.gridImageProgressBar?.let { hideProgressBar(it) }
                }

                override fun onLoadingFailed(
                    imageUri: String?,
                    view: View?,
                    failReason: FailReason?
                ) {
                    viewHolder.gridImageProgressBar?.let { hideProgressBar(it) }
                }

            })
        }


        return retView
    }

    inner class PhotoViewHolder {
        var gridImageImageView: SquareImage? = null
        var gridImageProgressBar: ProgressBar? = null
    }

}