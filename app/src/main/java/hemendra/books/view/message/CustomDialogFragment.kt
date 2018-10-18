package hemendra.books.view.message

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class CustomDialogFragment : DialogFragment() {

    companion object {
        const val MESSAGE = "msg"
    }

    private var onOkClicked: Runnable? = null
    fun setOnOkClicked(onOkClicked: Runnable?) {
        this.onOkClicked = onOkClicked
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context).setCancelable(true)
                .setMessage(arguments?.getString(MESSAGE) ?: "-")
                .setPositiveButton("OK") { _, _ -> onOkClicked?.run() }.create()
    }

}