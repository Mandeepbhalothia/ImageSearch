package com.mandeep.imagesearch.api

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.photos.SearchParameters
import com.mandeep.imagesearch.model.Photo
import com.mandeep.imagesearch.utilities.Constants

class FlickerInstance {

    companion object {
        private val flickr by lazy {
            Flickr(Constants.apiKey, Constants.secret, REST())
        }

        suspend fun getPhotosFromFlicker(name: String, totalImage: Int = 100): List<Photo> {

            val photoList = mutableListOf<Photo>()

            val photos = flickr.photosInterface
            val searchParameters = SearchParameters()
            searchParameters.media = "photos"
//        searchParameters.extras = Stream.of("media").collect(Collectors.toSet())
            searchParameters.text = name

            val results = photos.search(searchParameters, totalImage, 1)

            results?.let { photosList ->
                photosList.forEach { photo ->
                    photo?.let {
                        if (!it.smallUrl.isNullOrEmpty()) {
                            photoList.add(Photo(it.smallUrl, name))
                        }
                    }
                }
            }

            return photoList
        }
    }

}