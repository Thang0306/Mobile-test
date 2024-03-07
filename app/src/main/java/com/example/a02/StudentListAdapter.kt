package com.example.a02

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class StudentListAdapter(context: Context, private val studentList: List<Student>) :
    ArrayAdapter<Student>(context, R.layout.list_item_student, studentList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_student, parent, false)
        }

        val student = studentList[position]
        val nameTextView = view?.findViewById<TextView>(R.id.nameTV)
        val classTextView = view?.findViewById<TextView>(R.id.classTV)
        val birthdayGenderTextView = view?.findViewById<TextView>(R.id.birthdayGenderTV)

        nameTextView?.text = student.fullName
        classTextView?.text = student.className
        birthdayGenderTextView?.text = "${student.birthday} - ${student.gender}"

        return view!!
    }
}
