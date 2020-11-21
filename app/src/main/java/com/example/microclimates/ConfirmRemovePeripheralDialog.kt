package com.example.microclimates

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

class ConfirmRemovePeripheralDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val peripheralId = arguments?.getString("peripheralId")
            val peripheralName = arguments?.getString("peripheralName")

            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure that you want to remove $peripheralName?")
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        val result = Bundle().apply {
                            putString("peripheralId", peripheralId)
                        }
                        setFragmentResult("onPeripheralRemoveSubmit", result)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        println("cancel")
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}