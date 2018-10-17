package hemendra.books.view.listeners

import android.graphics.Bitmap

interface IImageHolder {

    /**
     * Gets called when image gets download successfully. If there is a failure, there will
     * be no callback.
     * @param bitmap The loaded bitmap.
     */
    fun gotImage(bitmap: Bitmap)

}