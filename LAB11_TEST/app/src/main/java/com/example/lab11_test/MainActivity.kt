package com.example.lab11_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val EditName = findViewById<EditText>(R.id.name)
        val EditEmail = findViewById<EditText>(R.id.email)
        val EditAge = findViewById<EditText>(R.id.age)
//        val output1 = findViewById<EditText>(R.id.output)
//
//        val readBtn = findViewById<EditText>(R.id.readBtn)
//        val writeBtn = findViewById<EditText>(R.id.writeBtn)

        readBtn.setOnClickListener{
            readFirebase()
        }

        writeBtn.setOnClickListener{
            var Name : String
            var Email : String
            var Age : Int = 10

            if(EditName.length() == 0 ) Name = "null" else Name = EditName.text.toString()
            if(EditEmail.length() == 0 ) Email = "null" else Email = EditEmail.text.toString()
            if(EditAge.length() == 0 ) Age = EditAge.text.toString().toString().toInt()

            writeFirebase(Name, Email, Age)
        }
    }

    fun readFirebase() {
        db.collection("users")
            .get()
            .addOnSuccessListener {
                    result -> for ( document in result )
                output1.append("${document.data} \n")
            }
            .addOnFailureListener {
                output1.append("Failure \n")
            }
    }

    fun writeFirebase(Name:String, Email:String, Age:Int) {
        var user = mapOf(
            "name" to Name,
            "email" to Email,
            "age" to Age
        )
        val colRef: CollectionReference = db.collection("users")
        val docRef: Task<DocumentReference> = colRef.add(user)
        docRef.addOnSuccessListener {
                documentReference -> output1.append("Success : " + "${documentReference.id}\n")
        }
        docRef.addOnFailureListener{
            output1.append("Failure \n")
        }
    }
}

