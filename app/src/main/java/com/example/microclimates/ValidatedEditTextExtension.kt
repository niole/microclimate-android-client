package com.example.microclimates

import android.widget.EditText
import androidx.core.widget.doOnTextChanged

fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.doOnTextChanged { text, _, _, _ ->
        if (validator(text.toString())) {
            this.error = null
        } else {
            this.error = message
        }
    }
    this.error = if (validator(this.text.toString())) null else message
}


