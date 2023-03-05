package com.example.lab10_ex04

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var option: SharedPreferences
    lateinit var userInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        option = getSharedPreferences("option", MODE_PRIVATE)
        userInfo = getSharedPreferences("user_info", MODE_PRIVATE)

        vBtnSave.setOnClickListener {
            userInfo.edit{
                putString("User Name", name.text.toString())
                name.setText(null)
            }
            Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT)
        }

        vSwitchAlarm.setOnCheckedChangeListener { compoundButton, b ->
            option.edit {
                putBoolean("alarm", vSwitchAlarm.isChecked)
            }

        }
    }
}