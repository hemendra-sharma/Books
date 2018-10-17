package hemendra.books.model.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build


class Utils {

    companion object {

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivity = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connectivity?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    for (network in it.allNetworks) {
                        val info = it.getNetworkInfo(network)
                        if (info != null && info.state == NetworkInfo.State.CONNECTED)
                            return true
                    }
                } else {
                    val allNetworkInfo = it.allNetworkInfo
                    allNetworkInfo?.let { info ->
                        for (i in info) {
                            if (i.state === NetworkInfo.State.CONNECTED) {
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }

    }

}