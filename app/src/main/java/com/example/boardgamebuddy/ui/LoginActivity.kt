package com.example.boardgamebuddy.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find views
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registrationText = findViewById<TextView>(R.id.registrationText)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set click listener for login button
        loginButton.setOnClickListener {
            loginUser()
        }

        // Set click listener for "Register here" text
        registrationText.setOnClickListener {
            // Navigate to RegisterActivity when "Register here" text is clicked
            val intent: Intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

            finish() // Finish LoginActivity to prevent going back to it
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("SIGNED IN", "User is already signed in")
        }
    }

    private fun loginUser() {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }

        // Sign in user with email and password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "Login failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}