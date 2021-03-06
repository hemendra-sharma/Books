package hemendra.books.model.utils

import android.support.annotation.WorkerThread
import android.util.Log
import java.io.*
import java.net.HttpURLConnection

class ContentDownloader {

    companion object {

        @WorkerThread
        fun getString(url: String, callback: ConnectionCallback): String? {
            var response: String? = null
            var bin: BufferedInputStream? = null
            var out: ByteArrayOutputStream? = null
            try {
                Log.i("getInputStream", "URL: $url")
                val conn: HttpURLConnection? = ConnectionBuilder.getConnection(url, "GET")
                conn?.let {
                    callback.onConnectionInitialized(it)
                    it.connect()
                    callback.onResponseCode(it.responseCode)
                    if(it.responseCode == HttpURLConnection.HTTP_OK) {
                        bin = BufferedInputStream(it.inputStream)
                        out = ByteArrayOutputStream()
                        val data = ByteArray(1024)
                        var read: Int = bin?.read(data) ?: -1
                        while (read != -1 && !Thread.interrupted()) {
                            out?.write(data, 0, read)
                            read = bin?.read(data) ?: -1
                        }
                        out?.let { o -> response = String(o.toByteArray()) }
                    }
                }
            } catch (e: InterruptedIOException) {
                callback.onInterrupted()
            } catch (e: IOException) {
                e.printStackTrace()
                callback.onError()
            } finally {
                bin?.close()
                out?.close()
            }
            return response
        }

        @WorkerThread
        fun getInputStream(url: String, callback: ConnectionCallback): InputStream? {
            try {
                Log.i("getInputStream", "URL: $url")
                val conn: HttpURLConnection? = ConnectionBuilder.getConnection(url, "GET")
                conn?.let {
                    callback.onConnectionInitialized(it)
                    it.connect()
                    callback.onResponseCode(it.responseCode)
                    if(it.responseCode == HttpURLConnection.HTTP_OK) {
                        return it.inputStream
                    }
                }
            } catch (e: InterruptedIOException) {
                callback.onInterrupted()
            } catch (e: IOException) {
                e.printStackTrace()
                callback.onError()
            }
            return null
        }

        @WorkerThread
        fun getByteArray(url: String, callback: ConnectionCallback): ByteArray? {
            var bytes: ByteArray? = null
            var bin: BufferedInputStream? = null
            var out: ByteArrayOutputStream? = null
            try {
                Log.i("getByteArray", "URL: $url")
                val conn: HttpURLConnection? = ConnectionBuilder.getConnection(url, "GET")
                conn?.let {
                    callback.onConnectionInitialized(it)
                    it.connect()
                    callback.onResponseCode(it.responseCode)
                    if(it.responseCode == HttpURLConnection.HTTP_OK) {
                        bin = BufferedInputStream(it.inputStream)
                        out = ByteArrayOutputStream()
                        val arr = ByteArray(1024)
                        var read = bin!!.read(arr)
                        while(read > 0) {
                            out!!.write(arr, 0, read)
                            read = bin!!.read(arr)
                        }
                        bin!!.close()
                        bytes = out!!.toByteArray()
                        out!!.close()
                    }
                }
            } catch (e: InterruptedIOException) {
                callback.onInterrupted()
            } catch (e: IOException) {
                e.printStackTrace()
                callback.onError()
            } finally {
                bin?.close()
                out?.close()
            }
            return bytes
        }

    }

}