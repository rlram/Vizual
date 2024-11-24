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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var etTextName: EditText
    private lateinit var etTextEmail: EditText
    private lateinit var etTextPassword: EditText
    private lateinit var etTextConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvSignIn: TextView

    private lateinit var inputName: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputConfirmPassword: TextInputLayout

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnBack = findViewById(R.id.btnBackSignUp)
        etTextName = findViewById(R.id.etTextNameSignUp)
        etTextEmail = findViewById(R.id.etTextEmailAddressSignUp)
        etTextPassword = findViewById(R.id.etTextPasswordSignUp)
        etTextConfirmPassword = findViewById(R.id.etTextConfirmPasswordSignUp)
        btnSignUp = findViewById(R.id.btnSignUpSignUP)
        tvSignIn = findViewById(R.id.tvSignInSignUp)

        inputName = findViewById(R.id.textInputLayoutName)
        inputEmail = findViewById(R.id.textInputLayoutEmail)
        inputPassword = findViewById(R.id.textInputLayoutPassword)
        inputConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword)

        firebaseAuth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users")

        btnBack.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignUp.setOnClickListener {
            val name = etTextName.text.toString()
            val email = etTextEmail.text.toString()
            val password = etTextPassword.text.toString()
            val confirmPassword = etTextConfirmPassword.text.toString()

            if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                inputName.error = "Name is required"
                inputEmail.error = "Email is required"
                inputPassword.error = "Password is required"
                inputConfirmPassword.error = "Confirm password is required"
            }
            if (name.isEmpty()) inputName.error = "Name is required"
            if (email.isEmpty()) inputEmail.error = "Email is required"
            if (password.isEmpty()) inputPassword.error = "Password is required"
            if (confirmPassword.isEmpty()) inputConfirmPassword.error = "Confirm password is required"

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (confirmPassword == password) {
                    signUpUser(name, email, password)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Password didn't match",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun signUpUser(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    val id = firebaseAuth.currentUser?.uid.toString()
                    val user = User(id, name, email)
                    databaseReference.child(id).setValue(user)
                        .addOnSuccessListener {
                            val config = ZegoUIKitPrebuiltCallInvitationConfig()
                            ZegoUIKitPrebuiltCallService.init(application, AppConstants.appId, AppConstants.appSign, email, email, config)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
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