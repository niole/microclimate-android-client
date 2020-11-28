package com.example.microclimates

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class Login : AppCompatActivity() {
    private val RC_SIGN_IN  = 50
    private val LOG_TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder().requestEmail().build()
        val client = GoogleSignIn.getClient(this, gso)

        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            val signInIntent: Intent = client.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email
            if (email != null) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("email", email)
                }
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "You must login in order to proceed.", Toast.LENGTH_LONG).show()
            }
        } catch (e: ApiException) {
            Log.w(LOG_TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}