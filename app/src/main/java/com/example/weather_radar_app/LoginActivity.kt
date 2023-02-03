package com.example.weather_radar_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_radar_app.fragments.ForgotPasswordFragment
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var tvCreateAccount: TextView
    private lateinit var tvForgotPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var etEmail: TextView
    private lateinit var etPassword: TextView
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.loginButton)
        tvCreateAccount = findViewById(R.id.tvCreateAccount)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        etEmail = findViewById(R.id.editTextEmail)
        etPassword = findViewById(R.id.editTextPassword)
        checkBox = findViewById(R.id.checkBoxPassword)

        checkBox.setOnClickListener {
           if (checkBox.text.toString() == "Show Password") {
               etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
               checkBox.text = "Hide Password"
           }
           else{
               etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
               checkBox.text = "Show Password"
           }
        }
        btnLogin.setOnClickListener {
            loginUser()
        }
        tvForgotPassword.setOnClickListener{
            ForgotPasswordFragment().show(supportFragmentManager,"forgotFragment")
        }
        tvCreateAccount.setOnClickListener{
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUser() {
        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()
        if (TextUtils.isEmpty(email)) {
            etEmail.error = ("Email cannot be empty")
            etEmail.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = ("Email is invalid")
            etEmail.requestFocus()
        }
        else if (TextUtils.isEmpty(password)) {
            etPassword.error = ("Password cannot be empty")
            etPassword.requestFocus()
        }
        else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified){
                        Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                         Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"Login Error:", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}