package hemendra.books.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import hemendra.books.model.cache.ImagesDB
import hemendra.books.model.listeners.IImageLoaderListener
import hemendra.books.model.utils.ConnectionCallback
import hemendra.books.model.utils.ContentDownloader
import hemendra.books.model.utils.CustomAsyncTask
import hemendra.books.view.listeners.IImageHolder
import java.net.HttpURLConnection

class ImageLoader(private var db: ImagesDB,
                  private var url: String,
                  private var callback: IImageHolder,
                  private var listener: IImageLoaderListener) :
        CustomAsyncTask<Void, Void, Bitmap?>() {

    private var connection: HttpURLConnection? = null
    val createdAt = System.currentTimeMillis()

    private var disconnectHandler: Handler? = null

    private val disconnectCallback: Handler.Callback = Handler.Callback {
        connection?.disconnect()
        true
    }

    override fun cancel(interrupt: Boolean) {
        disconnectHandler?.sendEmptyMessage(0)
        super.cancel(true)
    }

    override fun doInBackground(vararg params: Void): Bitmap? {
        if(Looper.myLooper() == null) Looper.prepare()

        var arr = db.getImage(url)
        arr?.let {
            return BitmapFactory.decodeByteArray(it, 0, it.size)
        }
        arr = ContentDownloader.getByteArray(url, object: ConnectionCallback {
            override fun onConnectionInitialized(conn: HttpURLConnection) {
                connection = conn
                disconnectHandler = Handler(disconnectCallback)
            }
            override fun onResponseCode(code: Int){}
            override fun onInterrupted(){}
            override fun onError(){}

        })
        arr?.let {
            db.insertImage(url, it)
            return BitmapFactory.decodeByteArray(it, 0, it.size)
        }
        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        if(result != null) callback.gotImage(result, url)
        listener.onExecutionFinished()
    }

}