package com.example.a02

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.IOException
import android.content.Intent
import android.app.Activity

class ChooseClassActivity : AppCompatActivity() {
    private val fileName = "class_list.txt"
    private lateinit var listView: ListView
    private lateinit var classListAdapter: ArrayAdapter<String>
    private val classList = mutableListOf<String>()
    private var isFromStudentInfoActivity: Boolean = false
    private var selectedClass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_class)

        listView = findViewById(R.id.customListView)
        classListAdapter = ArrayAdapter(this, R.layout.list_item_class, R.id.classTV, classList)
        listView.adapter = classListAdapter

        loadClassListFromFile()

        listView.setOnItemClickListener { parent, view, position, id ->
            selectedClass = classList[position]
        }

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            if (isFromStudentInfoActivity) {
                val intent = Intent().apply {
                    putExtra("selectedClass", selectedClass)
                }
                setResult(Activity.RESULT_OK, intent)
            } else {
                val intent = Intent().apply {
                    putExtra("selectedClass", selectedClass)
                }
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    private fun loadClassListFromFile() {
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.class_list)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                classList.add(line!!)
            }
            inputStream.close()
            classListAdapter.notifyDataSetChanged()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveClassListToFile() {
        try {
            val outputStreamWriter = OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE))
            for (className in classList) {
                outputStreamWriter.write("$className\n")
            }
            outputStreamWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}