package com.example.microclimates

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.SystemClock
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class DateRangePicker : DialogFragment() {
    companion object {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        fun newInstance(date: String): DialogFragment {
            return DateRangePicker().apply {
                arguments = Bundle().apply {
                    putString("defaultDate", date)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val defaultDate = arguments?.getString("defaultDate")
            val parsedDefaultDate = parser.parse(defaultDate)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = parsedDefaultDate.time

            val picker = DatePicker(context)
            picker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            val builder = AlertDialog.Builder(it)
            builder.setView(picker)

            builder
                .setPositiveButton("Submit",
                    DialogInterface.OnClickListener { dialog, id ->
                        val outCalendar = Calendar.getInstance()
                        outCalendar.set(Calendar.YEAR, picker.year)
                        outCalendar.set(Calendar.MONTH, picker.month)
                        outCalendar.set(Calendar.DAY_OF_MONTH, picker.dayOfMonth)

                        val selectedDate = parser.format(Date(outCalendar.timeInMillis))
                        setFragmentResult("onDateSubmit", bundleOf("selectedDate" to selectedDate))
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

