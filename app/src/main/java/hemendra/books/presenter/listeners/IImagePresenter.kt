package hemendra.books.presenter.listeners

import hemendra.books.view.listeners.IImageHolder

interface IImagePresenter {

    /**
     * Start loading the image for given URL.
     * @param url The image web url.
     * @param holder The view which is calling to download image.
     */
    fun getImage(url: String, holder: IImageHolder)

    /**
     * Abort the ongoing search process.
     */
    fun abort()

    /**
     * Destroy all the references and release memory.
     */
    fun destroy()
}