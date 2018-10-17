package hemendra.books.model.listeners

interface IImageSource {

    /**
     * Download or get image from local cache.
     * @param url The url to download.
     */
    fun getImage(url: String)

    /**
     * Abort the ongoing search process.
     */
    fun abort()

    /**
     * Destroy all the references and release memory.
     */
    fun destroy()
}