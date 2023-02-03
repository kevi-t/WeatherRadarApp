package com.example.weather_radar_app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var tvAccountSignin: TextView
    private lateinit var btnRegister: Button
    private lateinit var etName: TextView
    private lateinit var etEmail: TextView
    private lateinit var etPhoneNumber: TextView
    private lateinit var etCity: TextView
    private lateinit var etPassword: TextView
    private lateinit var checkBox: CheckBox
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_page)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User")

        checkBox = findViewById(R.id.checkBoxPassword)
        btnRegister = findViewById(R.id.registerButton)
        tvAccountSignin = findViewById(R.id.tvAccountSignin)
        etName = findViewById(R.id.editTextFullName)
        etEmail = findViewById(R.id.editTextEmail)
        etPhoneNumber = findViewById(R.id.editTextPhone)
        etCity = findViewById(R.id.editTextCity)
        etPassword = findViewById(R.id.editTextPassword)

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
        btnRegister.setOnClickListener {
             createUser()
        }
        tvAccountSignin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUser() {

        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()
        val name: String = etName.text.toString()
        val number: String = etPhoneNumber.text.toString()
        val city: String = etCity.text.toString()

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
        else if (etPassword.text.length<8) {
            etPassword.error = ("Password must be 8 characters or more long")
            etPassword.requestFocus()
        }
        else if (TextUtils.isEmpty(name)){
            etName.error = ("Name is empty")
            etName.requestFocus()
        }
        else if (TextUtils.isEmpty(number)){
            etPhoneNumber.error = ("Phone is empty")
            etPhoneNumber.requestFocus()
        }
        else if(etPhoneNumber.text.length!=10){
            etPhoneNumber.error = ("Short Phone Number")
            etPhoneNumber.requestFocus()
        }
        else if (TextUtils.isEmpty(city)){
            etCity.error = ("City is empty")
            etCity.requestFocus()
        }
        else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser

                        val user = User(name, email, number, city)
                        databaseReference.child("Users").child(firebaseUser!!.uid).setValue(user)

                        // Send a Verification Link
                        firebaseUser.sendEmailVerification().addOnSuccessListener {
                            Toast.makeText(this,"Verification sent check your email",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { it ->
                            Log.d(TAG,"onCreate: Verification not sent ${it.localizedMessage}")
                        }
                        // Logout the created user
                        auth.signOut()

                        Toast.makeText(this,"User Registration Successful",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }

            }.addOnFailureListener {
                Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}