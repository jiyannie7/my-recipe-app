package com.example.myfoodrecipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodrecipe.adapters.RecipeListAdapter
import com.example.myfoodrecipe.data.Recipe
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RecipeListActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        val userID = Firebase.auth.currentUser!!
        database = FirebaseDatabase.getInstance("https://myfoodrecipe-e34ec-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users").child(userID.uid).child("Recipes")

        val list = ArrayList<Recipe>()
        recyclerView = findViewById(R.id.rv_recipe)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecipeListAdapter(this,list)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        displayRecipe()
    }

    private fun displayRecipe(){
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Recipe>()
                if (snapshot.exists()){
                    for(data in snapshot.children ){
                        val model = data.getValue(Recipe::class.java)
                        list.add(model as Recipe)
                        val adapter = RecipeListAdapter(this@RecipeListActivity, list)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}