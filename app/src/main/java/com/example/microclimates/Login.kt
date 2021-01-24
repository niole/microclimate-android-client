package com.example.microclimates

import android.content.Intent
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN  = 50
    private val LOG_TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val googleSignInButton = findViewById<SignInButton>(R.id.google_sign_in_button)
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(this, gso)

        googleSignInButton.setOnClickListener {
            val signInIntent: Intent = client.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun startApp(email: String, jwt: String): Unit {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("jwt", jwt)
        }
        startActivity(intent)
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
            val jwt = account?.idToken

            if (account != null && email != null && jwt != null ) {
                firebaseAuthWithGoogle(email, account.idToken!!)
            } else {
                Toast.makeText(applicationContext, "You must sign in in order to proceed.", Toast.LENGTH_LONG).show()
            }
        } catch (e: ApiException) {
            Log.w(LOG_TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(email: String, idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val tokenResult = user?.getIdToken(true)
                    tokenResult?.addOnCompleteListener(this) { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result?.token
                            startApp(email, token!!)
                        } else {
                            Log.w(LOG_TAG, "Failed to get firebase token", tokenTask.exception)
                        }
                    }
                } else {
                    Log.w(LOG_TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
}