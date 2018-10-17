package hemendra.books.presenter

import hemendra.books.model.DataSourceFactory
import hemendra.books.model.listeners.IImageSource
import hemendra.books.presenter.listeners.IImagePresenter
import hemendra.books.view.listeners.IImageHolder

class ImagePresenter : IImagePresenter {

    companion object {

        var imagePresenter: ImagePresenter? = null

        fun getInstance(): ImagePresenter {
            if(imagePresenter == null) imagePresenter = ImagePresenter()
            return imagePresenter!!
        }

    }

    private val dataSourceFactory = DataSourceFactory()

    private var imageSource: IImageSource? = null

    init {
        imageSource = dataSourceFactory.getImageSource()
    }

    override fun getImage(url: String, holder: IImageHolder) {
        imageSource?.getImage(url, holder)
    }

    override fun abort() {
        imageSource?.abort()
    }

    override fun destroy() {
        abort()
        imageSource?.destroy()

        imageSource = null
    }

}