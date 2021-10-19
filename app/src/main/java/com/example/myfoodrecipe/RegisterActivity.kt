package com.example.myfoodrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfoodrecipe.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private val tag = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance("https://myfoodrecipe-e34ec-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users")

        registerAccount()
        hasRegistered()

    }
    private fun hasRegistered(){
        val tvHasRegistered : TextView = findViewById(R.id.tv_has_registered)
        tvHasRegistered.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun registerAccount(){
        val btnRegister : Button = findViewById(R.id.btn_register)
        val etEmail : EditText = findViewById(R.id.et_register_email)
        val etPassword : EditText = findViewById(R.id.et_register_pw)

        btnRegister.setOnClickListener {


            auth.createUserWithEmailAndPassword(etEmail.text.toString(),
                etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(tag, "Successful")
                        sendData()
                        Toast.makeText(applicationContext,
                            "Account created successfully",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(tag, "Unsuccessful")
                        Toast.makeText(this, "Register failed. Please try again",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun sendData(){
        val etEmail : EditText = findViewById(R.id.et_register_email)
        val etName : EditText = findViewById(R.id.et_register_name)
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val userId = Firebase.auth.currentUser
        val id = userId!!.uid

        val user = Users(name,email)
        reference.child(id).setValue(user)
    }
}