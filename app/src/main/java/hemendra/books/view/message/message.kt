package hemendra.books.view.message

import android.os.Bundle
import android.support.v4.app.FragmentManager

fun showMessage(fm: FragmentManager, msg: String, onOkClicked: Runnable? = null) {
    val dialog = CustomDialogFragment()
    dialog.setOnOkClicked(onOkClicked)
    val bundle = Bundle()
    bundle.putString(CustomDialogFragment.MESSAGE, msg)
    dialog.arguments = bundle
    dialog.show(fm, "DIALOG")
}
