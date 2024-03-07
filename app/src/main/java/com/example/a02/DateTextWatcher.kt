package com.example.a02

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DateTextWatcher(private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Không cần phải thực hiện bất kỳ hành động nào trước khi văn bản thay đổi
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        formatBirthday(s)
    }

    override fun afterTextChanged(s: Editable?) {
        // Không cần phải thực hiện bất kỳ hành động nào sau khi văn bản thay đổi
    }

    private fun formatBirthday(s: CharSequence?) {
        s?.let {
            if (it.length == 2 || it.length == 5) {
                editText.setText("$s/")
                editText.setSelection(editText.text.length)
            }
        }
    }
}
