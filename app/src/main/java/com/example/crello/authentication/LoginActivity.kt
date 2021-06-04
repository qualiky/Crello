package com.example.crello.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.crello.activities.BaseActivity
import com.example.crello.activities.MainActivity
import com.example.crello.R
import com.example.crello.databinding.ActivityLoginBinding
import com.example.crello.firebase.FirestoreClass
import com.example.crello.models.Users
import com.example.crello.utils.Utilities
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utilities().makeSplashScreenFullScreen(window)
        setUpActionBar()

        binding.tvAlreadyHaveAcc.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }

        binding.btnSignInActivity.setOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.applicationWindowToken, 0)

            signInUser()
        }

    }

    private fun signInUser() {
        val email = binding.etEmailSignIn.text.toString().trim()
        val password = binding.etPasswordSignIn.text.toString().trim()

        if(validateData(email, password)) {
            showProgressDialog()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        FirestoreClass().loadUserData(this)
                    } else {
                        dismissProgressDialog()
                        Toast.makeText(this,"Error logging in: ${it.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                        Toast.makeText(this,"Error logging in: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun validateData(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this,"Password cannot be empty!", Toast.LENGTH_SHORT).show()
                false
            } else -> true
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarSignIn)
        val actionBar = supportActionBar

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        binding.toolbarSignIn.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    fun userLoginSuccess(user: Users) {
        Toast.makeText(this,"Logged in successfully!", Toast.LENGTH_SHORT).show()
        dismissProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}