package com.example.a02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import android.app.Activity


class StudentInfoActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var classEditText: EditText
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var otherRadioButton: RadioButton
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var studentList: MutableList<Student>
    private var selectedClass: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_info)

        fullNameEditText = findViewById(R.id.fullNameEditText)
        birthdayEditText = findViewById(R.id.birthdayEditText)
        classEditText = findViewById(R.id.classEditText)
        maleRadioButton = findViewById(R.id.maleRadioButton)
        femaleRadioButton = findViewById(R.id.femaleRadioButton)
        otherRadioButton = findViewById(R.id.otherRadioButton)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        studentList = intent.getParcelableArrayListExtra<Student>("studentList")?.toMutableList() ?: mutableListOf()
        val position = intent.getIntExtra("position", -1)
        val student = studentList.getOrNull(position)

        birthdayEditText.addTextChangedListener(DateTextWatcher(birthdayEditText))

        // Populate EditTexts and RadioButtons with student data
        student?.let {
            fullNameEditText.setText(it.fullName)
            classEditText.setText(it.className)
            birthdayEditText.setText(it.birthday)
            when (it.gender) {
                "Male" -> maleRadioButton.isChecked = true
                "Female" -> femaleRadioButton.isChecked = true
                "Other" -> otherRadioButton.isChecked = true
            }
        }

        // Xử lý sự kiện click cho classEditText
        classEditText.setOnClickListener {
            // Mở ChooseClassActivity với yêu cầu là từ activity_student_info.xml
            val intent = Intent(this, ChooseClassActivity::class.java)
            intent.putExtra("isFromStudentInfoActivity", true)
            startActivityForResult(intent, CHOOSE_CLASS_REQUEST_CODE)
        }

        saveButton.setOnClickListener {
            saveStudentInfo(student)
        }
        deleteButton.setOnClickListener {
            deleteStudent(student)
        }
    }

    private fun saveStudentInfo(student: Student?) {
        student?.let {
            val position = intent.getIntExtra("position", -1)
            if (position != -1) {
                studentList[position] = Student(
                    fullNameEditText.text.toString(),
                    classEditText.text.toString(),
                    birthdayEditText.text.toString(),
                    getSelectedGender()
                )
                updateStudentListFile(studentList)
                val intent = Intent().apply {
                    putExtra("updatedStudentList", ArrayList(studentList))
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_CLASS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedClass = data?.getStringExtra("selectedClass")
            classEditText.setText(selectedClass)
        }
    }

    private fun deleteStudent(student: Student?) {
        student?.let {
            val position = studentList.indexOf(student)
            if (position != -1) {
                studentList.removeAt(position)
                updateStudentListFile(studentList)
                // Gửi kết quả về MainActivity
                val intent = Intent().apply {
                    putExtra("updatedStudentList", ArrayList(studentList))
                }
                setResult(RESULT_OK, intent)
            }
            finish()
        }
    }

    private fun updateStudentListFile(studentList: List<Student>) {
        try {
            val filePath = File(filesDir, MainActivity.STUDENT_FILE_NAME)
            val writer = BufferedWriter(FileWriter(filePath))

            for (student in studentList) {
                val studentInfo = "${student.fullName},${student.className},${student.birthday},${student.gender}\n"
                writer.write(studentInfo)
            }

            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSelectedGender(): String {
        return when (genderRadioGroup.checkedRadioButtonId) {
            R.id.maleRadioButton -> "Male"
            R.id.femaleRadioButton -> "Female"
            R.id.otherRadioButton -> "Other"
            else -> ""
        }
    }

    companion object {
        const val CHOOSE_CLASS_REQUEST_CODE = 1
    }
}
