package com.example.microclimates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class DatePickerButtonViewModel : ViewModel() {
    val selectedDate: MutableLiveData<Date> = MutableLiveData()

    fun setSelectedDate(newDate: Date): Unit {
        selectedDate.value = newDate
    }

    fun getSelectedDate(): LiveData<Date> {
        return selectedDate
    }
}

class DatePickerButton : Fragment() {
    companion object {
        val DEFAULT_VALUE_ARG_KEY = "defaultValue"

        fun newInstance(defaultDate: String): Fragment {
            return DatePickerButton().apply {
                arguments = Bundle().apply {
                    putString(DEFAULT_VALUE_ARG_KEY, defaultDate)
                }
            }
        }
    }

    private var onChangeListener: (date: Date) -> Unit = {}

    private val model: DatePickerButtonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val providedDefault = arguments?.getString(DEFAULT_VALUE_ARG_KEY)
        if (providedDefault != null) {
            model.setSelectedDate(DateRangePicker.parser.parse(providedDefault))
        } else {
            val defaultCalendarDate = Date(Calendar.getInstance().timeInMillis)
            model.setSelectedDate(defaultCalendarDate)
        }

        val lifecycleOwner = LifecycleOwner { this.lifecycle }
        childFragmentManager.setFragmentResultListener("onDateSubmit", lifecycleOwner) { _, bundle ->
            println("received date")
            val selectedDate = bundle.getString("selectedDate")
            if (selectedDate != null) {
                model.setSelectedDate(DateRangePicker.parser.parse(selectedDate))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.date_picker_button_fragment, container)

        model.getSelectedDate().observeForever { selectedDate ->
            println("date updated internally $selectedDate")
            view?.post {
                onChangeListener(selectedDate)
            }
            val button = layout.findViewById<Button>(R.id.date_value_button)
            button.text = selectedDate.toString()
        }

        val button = layout.findViewById<Button>(R.id.date_value_button)
        button.setOnClickListener {
            val dateRangePicker = DateRangePicker.newInstance(
                DateRangePicker.parser.format(model.selectedDate.value!!)
            )
            dateRangePicker.show(childFragmentManager, "DatePickerDialog")
        }

        return layout
    }

    fun setOnChangeListener(handler: (date: Date) -> Unit): Unit {
        onChangeListener = handler
    }
}