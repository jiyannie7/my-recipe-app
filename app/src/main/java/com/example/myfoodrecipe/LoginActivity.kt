package com.example.myfoodrecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val tag = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        loginAccount()
        notRegistered()

    }
    private fun notRegistered(){
        val tvNotRegistered : TextView = findViewById(R.id.tv_not_registered)
        tvNotRegistered.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun loginAccount(){

        val btnLogin : Button = findViewById(R.id.btn_login)
        val etEmail : TextView = findViewById(R.id.et_email)
        val etPassword : TextView = findViewById(R.id.et_password)

        btnLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag,"Authentication Successful")
                    val user = auth.currentUser
                    startActivity(Intent(this, RecipeListActivity::class.java))
                    updateUI(user)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Incorrect email address or password", Toast.LENGTH_LONG).show()
                    Log.d(tag,"Authentication failed")
                    updateUI(null)
                }
            }
       }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI (currentUser: FirebaseUser?){

        if(currentUser != null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

}