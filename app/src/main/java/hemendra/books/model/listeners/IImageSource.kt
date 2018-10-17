package hemendra.books.model.listeners

import hemendra.books.view.listeners.IImageHolder

interface IImageSource {

    /**
     * Download or get image from local cache.
     * @param url The url to download.
     * @param holder The view which is calling the image URL.
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