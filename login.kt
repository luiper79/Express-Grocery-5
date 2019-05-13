package com.example.deliveryapp

import android.R
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class login : AppCompatActivity() {
    internal var Email: EditText
    internal var Password: EditText
    internal var LogInButton: Button
    internal var RegisterButton: Button
    internal var mAuth: FirebaseAuth
    internal var mAuthListner: FirebaseAuth.AuthStateListener? = null
    internal var mUser: FirebaseUser? = null
    internal var email: String
    internal var password: String
    internal var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogInButton = findViewById<View>(R.id.buttonLogin) as Button

        RegisterButton = findViewById<View>(R.id.buttonRegister) as Button

        Email = findViewById<View>(R.id.editEmail) as EditText
        Password = findViewById<View>(R.id.editPassword) as EditText
        dialog = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        mUser = FirebaseAuth.getInstance().getCurrentUser()
        mAuthListner = object : FirebaseAuth.AuthStateListener() {
            fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                if (mUser != null) {
                    val intent = Intent(this@login, DashboardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    Log.d(TAG, "AuthStateChanged:Logout")
                }

            }
        }
        // LogInButton.setOnClickListener((View.OnClickListener) this);
        //RegisterButton.setOnClickListener((View.OnClickListener) this);
        //Adding click listener to log in button.
        LogInButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                // Calling EditText is empty or no method.
                userSign()


            }
        })

        // Adding click listener to register button.
        RegisterButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                // Opening new user registration activity using intent on button click.
                val intent = Intent(this@login, Register::class.java)
                startActivity(intent)

            }
        })

    }

    override fun onStart() {
        super.onStart()
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner)

    }

    override fun onStop() {
        super.onStop()
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner)
        }

    }

    override fun onBackPressed() {
        super@login.finish()
    }


    private fun userSign() {
        email = Email.getText().toString().trim({ it <= ' ' })
        password = Password.getText().toString().trim({ it <= ' ' })
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@login, "Enter the correct Email", Toast.LENGTH_SHORT).show()
            return
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@login, "Enter the correct password", Toast.LENGTH_SHORT).show()
            return
        }
        dialog.setMessage("Loging in please wait...")
        dialog.setIndeterminate(true)
        dialog.show()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (!task.isSuccessful()) {
                        dialog.dismiss()

                        Toast.makeText(this@login, "Login not successfull", Toast.LENGTH_SHORT).show()

                    } else {
                        dialog.dismiss()

                        checkIfEmailVerified()

                    }
                }
            })

    }

    //This function helps in verifying whether the email is verified or not.
    private fun checkIfEmailVerified() {
        val users = FirebaseAuth.getInstance().getCurrentUser()
        val emailVerified = users.isEmailVerified()
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show()
            mAuth.signOut()
            finish()
        } else {
            Email.getText().clear()

            Password.getText().clear()
            val intent = Intent(this@login, DashboardActivity::class.java)

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(userEmail, email)

            startActivity(intent)

        }
    }

    companion object {
        val userEmail = ""

        val TAG = "LOGIN"
    }

}
