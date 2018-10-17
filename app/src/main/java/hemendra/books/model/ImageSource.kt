package hemendra.books.model

import hemendra.books.model.listeners.IImageSource
import hemendra.books.view.listeners.IImageHolder

class ImageSource : IImageSource {

    companion object {

        private var imageSource: ImageSource? = null

        fun getInstance() : ImageSource {
            if(imageSource == null) imageSource = ImageSource()
            return imageSource!!
        }

    }

    override fun getImage(url: String, holder: IImageHolder) {

    }

    override fun abort() {

    }

    override fun destroy() {
        abort()
        imageSource = null
    }
}