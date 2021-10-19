package com.example.myfoodrecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myfoodrecipe.data.Recipe
import com.example.myfoodrecipe.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference  : DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityMainBinding
    lateinit var imgUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setContentView(binding.root)
       //setContentView(R.layout.activity_main)

        binding.btnChoose.setOnClickListener {
            selectImage()
        }
        binding.rAddBtn.setOnClickListener {
            sendRecipeData()

            startActivity(Intent(this,RecipeListActivity::class.java))
        }
        binding.rViewBtn.setOnClickListener {
            startActivity(Intent(this,RecipeListActivity::class.java))
        }

        auth = Firebase.auth
        val userID = Firebase.auth.currentUser!!
        database = FirebaseDatabase.getInstance("https://myfoodrecipe-e34ec-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users").child(userID.uid).child("Recipes")

    }

    private fun selectImage(){

        val intent = Intent()
        intent.type = "pictures/"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            imgUri = data?.data!!
            binding.imgView.setImageURI(imgUri)

        }
    }

    private fun sendRecipeData(){

        val type = binding.etType.text.toString().trim()
        val name = binding.etRecipeName.text.toString().trim()
        val ingredients = binding.etIngredient.text.toString().trim()
        val steps = binding.etSteps.text.toString().trim()
        val rid = reference.push().key

        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val now = Date()
        val file = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("pictures/$file")

        storage.putFile(imgUri).addOnSuccessListener {
            binding.imgView.setImageURI(null)
        }.addOnFailureListener {
            Toast.makeText(this@MainActivity,"Failed",Toast.LENGTH_SHORT).show()
        }

        reference.child(rid!!).setValue(Recipe(rid,type, name, ingredients, steps))

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true

}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.logout -> {
                Firebase.auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                onBackPressed()
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

}
