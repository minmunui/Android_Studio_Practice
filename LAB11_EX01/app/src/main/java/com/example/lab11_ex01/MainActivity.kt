package com.example.lab11_ex01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lab11_ex01.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore
    var oSysMainLoop = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adocRef = db.collection("player").document("test@gmail.com")

        adocRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                binding.textView.text = snapshot.data!!["xloc"].toString()
            }
        })
        if (oSysMainLoop == 0) {
            oSysMainLoop = 1
            timer(period = 1500, initialDelay = 1000)
            {
                if (oSysMainLoop != 1) {
                    cancel()
                }
                val axloc = hashMapOf("xloc" to (0..100).random())
                db.collection("player").document("test@gmail.com").set(axloc)
                    .addOnSuccessListener { result ->
                        Log.d("Suc", "Success!\n")
                    }
                    .addOnFailureListener {
                        Log.w("Err", "Fail! \n")
                    }
            }
        }
    }
}