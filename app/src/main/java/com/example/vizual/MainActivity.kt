package com.example.vizual

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var tvUserName: TextView
    private lateinit var btnSignOut: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoContact: TextView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ContactAdapter

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private val contactList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tvUserName = findViewById(R.id.tvUserName)
        btnSignOut = findViewById(R.id.btnSignOut)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBarMain)
        tvNoContact = findViewById(R.id.tvNoContacts)

        firebaseAuth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users")

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = ContactAdapter(contactList)
        recyclerView.adapter = adapter

        databaseReference.child(firebaseAuth.currentUser?.uid.toString()).get()
            .addOnSuccessListener {
                tvUserName.text = it.child("name").value.toString()
            }

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    progressBar.visibility = View.GONE
                    for (item in snapshot.children) {
                        val id = item.child("id").value.toString()
                        val name = item.child("name").value.toString()
                        val userName = item.child("userName").value.toString()

                        contactList.add(User(id, name, userName))
                        adapter.notifyItemInserted(contactList.size)
                    }
                } else {
                    progressBar.visibility = View.GONE
                    tvNoContact.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    error.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}