package com.example.a02

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStreamWriter
import android.widget.RadioGroup

class AddStudentInfoActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var classEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var saveButton: Button
    private var selectedClass: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student_info)

        fullNameEditText = findViewById(R.id.fullNameEditText)
        classEditText = findViewById(R.id.classEditText)
        birthdayEditText = findViewById(R.id.birthdayEditText)
        saveButton = findViewById(R.id.saveButton)

        val radioGroup: RadioGroup = findViewById(R.id.genderRadioGroup)
        var selectedGender = ""

        birthdayEditText.addTextChangedListener(DateTextWatcher(birthdayEditText))

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedGender = when (checkedId) {
                R.id.maleRadioButton -> "Male"
                R.id.femaleRadioButton -> "Female"
                R.id.otherRadioButton -> "Other"
                else -> ""
            }
        }

        // Xử lý sự kiện click cho classEditText
        classEditText.setOnClickListener {
            // Mở ChooseClassActivity với yêu cầu là từ activity_student_info.xml
            val intent = Intent(this, ChooseClassActivity::class.java)
            intent.putExtra("isFromAddStudentInfoActivity", true)
            startActivityForResult(intent, CHOOSE_CLASS_REQUEST_CODE)
        }

        saveButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val className = classEditText.text.toString()
            val birthday = birthdayEditText.text.toString()
            val gender = selectedGender
            val studentInfo = "$fullName,$className,$birthday,$gender\n"
            saveStudentInfo(studentInfo)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_CLASS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedClass = data?.getStringExtra("selectedClass")
            classEditText.setText(selectedClass)
        }
    }

    private fun saveStudentInfo(studentInfo: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(openFileOutput(MainActivity.STUDENT_FILE_NAME, MODE_APPEND))
            outputStreamWriter.write(studentInfo)
            outputStreamWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val CHOOSE_CLASS_REQUEST_CODE = 1
    }
}
