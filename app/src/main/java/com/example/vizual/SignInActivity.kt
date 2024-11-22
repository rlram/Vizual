package com.example.vizual

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignInActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var etTextEmail: EditText
    private lateinit var etTextPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvSignUp: TextView

    private lateinit var textLayoutEmail: TextInputLayout
    private lateinit var textLayoutPassword: TextInputLayout

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnBack = findViewById(R.id.btnBackSignIn)
        etTextEmail = findViewById(R.id.etTextEmailAddressSignIn)
        etTextPassword = findViewById(R.id.etTextPasswordSignIn)
        btnSignIn = findViewById(R.id.btnSignInSignIn)
        tvSignUp = findViewById(R.id.tvSignUpSignIn)

        textLayoutEmail = findViewById(R.id.textInputLayoutEmailSignIn)
        textLayoutPassword = findViewById(R.id.textInputLayoutPasswordSignIn)

        firebaseAuth = Firebase.auth

        btnBack.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignIn.setOnClickListener {
            val email = etTextEmail.text.toString()
            val password = etTextPassword.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                textLayoutEmail.error = "Email is required"
                textLayoutPassword.error = "Password is required"
            }
            if (email.isEmpty()) textLayoutEmail.error = "Email is required"
            if (password.isEmpty()) textLayoutPassword.error = "Password is required"

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}