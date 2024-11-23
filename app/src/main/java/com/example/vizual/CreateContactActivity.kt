package com.example.vizual

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateContactActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var etTextPersonName: EditText
    private lateinit var etTextPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnBack = findViewById(R.id.btnBackCreateContact)
        etTextPersonName = findViewById(R.id.etTextPersonName)
        etTextPhone = findViewById(R.id.etTextPhone)
        btnSave = findViewById(R.id.btnSave)

        firebaseAuth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("contacts")

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSave.setOnClickListener {
            val personName = etTextPersonName.text.toString()
            val phone = etTextPhone.text.toString()

            if (personName.isEmpty() && phone == "+91") {
                etTextPersonName.error = "Person name is required"
                etTextPhone.error = "Phone is required"
            }
            if (personName.isEmpty()) etTextPersonName.error = "Person name is required"
            if (phone == "+91") etTextPhone.error = "Phone is required"

            if (personName.isNotEmpty() && phone != "+91") {
                saveContact(personName, phone)
            }
        }
    }

    private fun saveContact(personName: String, phone: String) {
        val id = System.currentTimeMillis().toString()
        val uId = firebaseAuth.currentUser?.uid.toString()
        val contact = Contact(id, personName, phone, uId)
        databaseReference.child(id).setValue(contact)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
}