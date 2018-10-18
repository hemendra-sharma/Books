package hemendra.books.model

import android.os.Handler
import android.os.Looper
import hemendra.books.data.Book
import hemendra.books.model.listeners.BooksLoaderListener
import hemendra.books.model.utils.ConnectionCallback
import hemendra.books.model.utils.ContentDownloader
import hemendra.books.model.utils.CustomAsyncTask
import java.net.HttpURLConnection
import java.net.URLEncoder

class GetBooks(private val listener: BooksLoaderListener):
        CustomAsyncTask<Any, Void, ArrayList<Book>?>() {

    private var connection: HttpURLConnection? = null
    private var reason = "Unknown"

    private var disconnectHandler: Handler? = null

    private val disconnectCallback: Handler.Callback = Handler.Callback {
        connection?.disconnect()
        true
    }

    override fun cancel(interrupt: Boolean) {
        disconnectHandler?.sendEmptyMessage(0)
        super.cancel(true)
    }

    override fun doInBackground(vararg params: Any): ArrayList<Book>? {
        if(Looper.myLooper() == null) Looper.prepare()

        try {
            val query = URLEncoder.encode(params[0] as String, "UTF-8")
            val apiKey = params[1] as String
            val pageNumber = params[2] as Int
            val startIndex = (pageNumber - 1) * 10

            val url = "https://www.googleapis.com/books/v1/volumes?q=$query&key=$apiKey&startIndex=$startIndex"

            val json = ContentDownloader.getString(url, object : ConnectionCallback {
                override fun onConnectionInitialized(conn: HttpURLConnection) {
                    connection = conn
                    disconnectHandler = Handler(disconnectCallback)
                }

                override fun onResponseCode(code: Int) {
                    codeToReason(code)
                }

                override fun onInterrupted() {
                    reason = "Aborted"
                }

                override fun onError() {
                    reason = "Network Error"
                }
            })

            json?.let {
                return BooksParser.parseBooks(it)
            }

            return null
        } finally {
            Looper.myLooper()?.quit()
            disconnectHandler = null
        }
    }

    override fun onPostExecute(result: ArrayList<Book>?) {
        result?.let {
            listener.onSearchResults(it)
        } ?: listener.onSearchFailed(reason)
    }

    fun codeToReason(code: Int) {
        when (code) {
            HttpURLConnection.HTTP_NOT_FOUND -> reason = "$code (Not Found)"
            HttpURLConnection.HTTP_BAD_GATEWAY -> reason = "$code (Bad Gateway)"
            HttpURLConnection.HTTP_BAD_METHOD -> reason = "$code (Bad Method)"
            HttpURLConnection.HTTP_BAD_REQUEST -> reason = "$code (Bad Request)"
            HttpURLConnection.HTTP_CLIENT_TIMEOUT -> reason = "$code (Client Timeout)"
            HttpURLConnection.HTTP_CONFLICT -> reason = "$code (Conflict)"
            HttpURLConnection.HTTP_ENTITY_TOO_LARGE ->
                reason = "$code (Entity Too Large)"
            HttpURLConnection.HTTP_FORBIDDEN -> reason = "$code (Forbidden)"
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT ->
                reason = "$code (Gateway Timeout)"
            HttpURLConnection.HTTP_GONE -> reason = "$code (Gone)"
            HttpURLConnection.HTTP_INTERNAL_ERROR -> reason = "$code (Internal Error)"
            HttpURLConnection.HTTP_NOT_ACCEPTABLE ->
                reason = "$code (Not Acceptable)"
            HttpURLConnection.HTTP_NOT_AUTHORITATIVE ->
                reason = "$code (Not Authoritative)"
            HttpURLConnection.HTTP_NOT_IMPLEMENTED ->
                reason = "$code (Not Implemented)"
            HttpURLConnection.HTTP_OK -> {}
            HttpURLConnection.HTTP_UNAUTHORIZED ->
                reason = "$code (Unauthorized)"
            else -> reason = "HTTP Response Code: $code"
        }
    }

}