package com.example.crello.firebase

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.crello.activities.MainActivity
import com.example.crello.activities.MyProfileActivity
import com.example.crello.authentication.LoginActivity
import com.example.crello.authentication.SignupActivity
import com.example.crello.models.Users
import com.example.crello.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private var mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignupActivity, userInfo: Users, context: Context) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Could not create user profile: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    fun loadUserData(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(Users::class.java)
                when(activity) {
                    is LoginActivity -> {
                        if(loggedUser != null) {
                            activity.userLoginSuccess(loggedUser)
                        }
                    }
                    is MainActivity -> {
                        if(loggedUser != null) {
                            activity.updateNavigationUserDetails(loggedUser)
                        }
                    }
                    is MyProfileActivity -> {
                        activity.setUserDataUpInUI(loggedUser!!)
                    }
                }
            }.addOnFailureListener {
                when(activity) {
                    is LoginActivity -> {
                        activity.dismissProgressDialog()
                    }
                    is MainActivity -> {
                        activity.dismissProgressDialog()
                }
            }
        }
    }

    fun getCurrentUserId() : String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}