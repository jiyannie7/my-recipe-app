package com.example.myfoodrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myfoodrecipe.data.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference  : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val userID = Firebase.auth.currentUser!!
        database = FirebaseDatabase.getInstance("https://myfoodrecipe-e34ec-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users").child(userID.uid).child("Recipes")

        addData()
        viewList()
    }
    private fun viewList(){
        val btnView : Button = findViewById(R.id.r_view_btn)
        btnView.setOnClickListener {

            startActivity(Intent(this,RecipeListActivity::class.java))
        }
    }
    private fun addData(){
        val btnSubmit : Button = findViewById(R.id.r_add_btn)
        btnSubmit.setOnClickListener {

            sendRecipeData()

        }
    }

    private fun sendRecipeData(){

        val etTypes : EditText = findViewById(R.id.et_type)
        val etRecipeName : EditText = findViewById(R.id.et_recipe_name)
        val etIngredients : EditText = findViewById(R.id.et_ingredient)
        val etSteps : EditText = findViewById(R.id.et_steps)

        val type = etTypes.text.toString().trim()
        val name = etRecipeName.text.toString().trim()
        val ingredients = etIngredients.text.toString().trim()
        val steps = etSteps.text.toString().trim()
        val rid = reference.push().key

        reference.child(rid!!).setValue(Recipe(rid,type, name, ingredients, steps))

    }
}