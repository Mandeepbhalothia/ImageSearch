package com.mandeep.imagesearch.application

import android.app.Application
import android.os.StrictMode
import com.mandeep.imagesearch.utilities.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader

class ImageSearch: Application() {

    override fun onCreate() {
        super.onCreate()

        // if we want to hit api on main thread also
        /*val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)*/

        //init universalImageLoader
        initImageLoader()
    }

    private fun initImageLoader(){
        val universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.getConfig())
    }

}