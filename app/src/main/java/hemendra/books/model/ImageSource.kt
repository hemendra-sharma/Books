package hemendra.books.model

import android.content.Context
import hemendra.books.model.cache.ImagesDB
import hemendra.books.model.listeners.IImageLoaderListener
import hemendra.books.model.listeners.IImageSource
import hemendra.books.view.listeners.IImageHolder

class ImageSource(context: Context) : IImageSource, IImageLoaderListener {

    companion object {

        private var imageSource: ImageSource? = null

        fun getInstance(context: Context) : ImageSource {
            if(imageSource == null) imageSource = ImageSource(context)
            return imageSource!!
        }

    }

    private val db = ImagesDB(context)
    private val downloadPool = DownloadPool()

    override fun getImage(url: String, holder: IImageHolder) {
        downloadPool.newDownload(ImageLoader(db, url, holder, this))
    }

    override fun onExecutionFinished() {
        downloadPool.restackElementFromQueue()
    }

    override fun abort() {
        downloadPool.abortAllDownloads()
    }

    override fun destroy() {
        abort()
        db.close()
        imageSource = null
    }
}