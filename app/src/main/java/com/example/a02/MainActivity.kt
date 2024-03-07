package com.example.a02

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var studentListView: ListView
    private lateinit var addStudentButton: ImageButton
    private lateinit var studentListAdapter: StudentListAdapter
    private val studentList = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentListView = findViewById(R.id.customListView)
        addStudentButton = findViewById(R.id.addStudentButton)

        // Set up the adapter for the ListView
        studentListAdapter = StudentListAdapter(this, studentList)
        studentListView.adapter = studentListAdapter

        // Load student data from internal storage
        loadStudentList()

        // Set up click listener for add student button
        addStudentButton.setOnClickListener {
            val intent = Intent(this, AddStudentInfoActivity::class.java)
            startActivityForResult(intent, ADD_STUDENT_REQUEST_CODE)
        }

        studentListView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, StudentInfoActivity::class.java)
            intent.putExtra("studentList", ArrayList(studentList))
            intent.putExtra("student", studentList[position])
            intent.putExtra("position", position)
            startActivityForResult(intent, STUDENT_INFO_REQUEST_CODE)
        }
    }

    // Function to load student list from internal storage
    private fun loadStudentList() {
        try {
            val inputStream = openFileInput(STUDENT_FILE_NAME)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                val studentInfo = line.split(",")
                val student = Student(studentInfo[0], studentInfo[1], studentInfo[2], studentInfo[3])
                studentList.add(student)
                line = reader.readLine()
            }
            reader.close()
            studentListAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                STUDENT_INFO_REQUEST_CODE -> handleStudentInfoResult(data)
                ADD_STUDENT_REQUEST_CODE -> handleAddStudentResult()
            }
        }
    }

    private fun handleStudentInfoResult(data: Intent?) {
        val deletedPosition = data?.getIntExtra("deletedPosition", -1)
        deletedPosition?.let {
            if (it != -1) {
                studentList.removeAt(it)
                studentListAdapter.notifyDataSetChanged()
            }
        }
        studentList.clear()
        loadStudentList()
    }

    private fun handleAddStudentResult() {
        studentList.clear()
        loadStudentList()
    }

    companion object {
        const val ADD_STUDENT_REQUEST_CODE = 1
        const val STUDENT_INFO_REQUEST_CODE = 2
        const val STUDENT_FILE_NAME = "student_list.txt"
    }
}
