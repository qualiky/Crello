package com.example.crello.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.crello.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    private var doublePressToExitPressedOnce = false

    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }

    fun showProgressDialog() {
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.show()
    }

    fun dismissProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss()
        }
    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackPressedToExit() {
        if(doublePressToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doublePressToExitPressedOnce = true
        Snackbar.make(findViewById(R.id.content),"Please click back once again to exit!",Snackbar.LENGTH_LONG).show()

        Handler(Looper.myLooper()!!).postDelayed({
            doublePressToExitPressedOnce = false
        }, 2000)
    }

    fun showToast(message: String, context: Context) {
        val toastBar = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}