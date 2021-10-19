package com.example.myfoodrecipe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodrecipe.adapters.RecipeListAdapter
import com.example.myfoodrecipe.data.Recipe
import com.example.myfoodrecipe.databinding.ActivityMainBinding
import com.example.myfoodrecipe.databinding.ActivityRecipeListBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class RecipeListActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var reference : DatabaseReference
    private lateinit var recipeList : ArrayList<Recipe>
    lateinit var binding: ActivityRecipeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        recyclerView = findViewById(R.id.rv_recipe)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recipeList = arrayListOf()

        displayRecipe()
    }

    private fun displayRecipe(){

        val userID = Firebase.auth.currentUser!!
        reference = FirebaseDatabase.getInstance("https://myfoodrecipe-e34ec-default-rtdb.asia-southeast1.firebasedatabase.app").
        getReference("Users").child(userID.uid).child("Recipes")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(data in snapshot.children ){

                        val recipe = data.getValue(Recipe::class.java)
                        recipeList.add(recipe!!)

                    }
                    recyclerView.adapter = RecipeListAdapter(recipeList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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