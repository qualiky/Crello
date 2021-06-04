package com.example.crello.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.crello.activities.BaseActivity
import com.example.crello.R
import com.example.crello.databinding.ActivitySignupBinding
import com.example.crello.firebase.FirestoreClass
import com.example.crello.models.Users
import com.example.crello.utils.Utilities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : BaseActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utilities().makeSplashScreenFullScreen(window)
        setUpActionBar()

        binding.tvAlreadyHaveAcc.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.btnSignUpActivity.setOnClickListener {

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.applicationWindowToken, 0)

            registerUser()
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarSignUP)
        val actionBar = supportActionBar

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        binding.toolbarSignUP.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showToast("Error: Please enter your username!", this)
                false }
            TextUtils.isEmpty(email) -> {
                showToast("Error: Email field cannot be empty!", this)
                false }
            TextUtils.isEmpty(password) -> {
                showToast("Error: Password is required to sign in!", this)
                false
            }
            else -> {
                true}
        }
    }

    private fun registerUser() {
        val name: String = binding.etUsername.text.toString().trim()
        val email: String = binding.etEmail.text.toString().trim()
        val password: String = binding.etPassword.text.toString().trim()

        if(validateForm(name, email, password)){
            showProgressDialog()
            Firebase.auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) {
                    if(it.isSuccessful) {
                        dismissProgressDialog()
                        val fbUser = it.result!!.user!!
                        val registeredEmail = fbUser.email!!
                        val user = Users(fbUser.uid, name, registeredEmail)
                        FirestoreClass().registerUser(this,user, this)

                    } else {
                        showToast("Failed to create account!", this)
                        }
            }.addOnFailureListener(this) {
                    dismissProgressDialog()
                    showToast("Failed to create account: Error -> ${it.localizedMessage}", this)
                }
        }
    }

    fun userRegisteredSuccess() {
        Toast.makeText(this,"User created successfully!", Toast.LENGTH_SHORT).show()
        dismissProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

}