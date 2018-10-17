package hemendra.books.view.listeners

import android.graphics.Bitmap

interface IImageHolder {

    /**
     * Gets called when image gets download successfully. If there is a failure, there will
     * be no callback.
     * @param bitmap The loaded bitmap.
     * @param url The web URL called to get this image.
     */
    fun gotImage(bitmap: Bitmap, url: String)

}