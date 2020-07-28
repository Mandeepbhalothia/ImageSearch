package com.mandeep.imagesearch.utilities

import android.content.Context
import android.graphics.Bitmap
import com.mandeep.imagesearch.R
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer

class UniversalImageLoader(private val context: Context) {

    private val defaultImage = R.drawable.ic_launcher_background

    fun getConfig(): ImageLoaderConfiguration{
        val defaultOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(defaultImage)
            .showImageForEmptyUri(defaultImage)
            .showImageOnFail(defaultImage)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(FadeInBitmapDisplayer(300)).build()

        return ImageLoaderConfiguration.Builder(context)
            .defaultDisplayImageOptions(defaultOptions)
            .memoryCache(WeakMemoryCache())
            .diskCacheSize(100 * 1024 * 1024).build()
    }

}