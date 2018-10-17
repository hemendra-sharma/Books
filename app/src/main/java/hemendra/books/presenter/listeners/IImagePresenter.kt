package hemendra.books.presenter.listeners

interface IImagePresenter {

    /**
     * Start loading the image for given URL.
     * @param url The image web url.
     */
    fun getImage(url: String)

}