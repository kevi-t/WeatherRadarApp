package com.example.weather_radar_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.weather_radar_app.databinding.ForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
class ForgotPasswordFragment : DialogFragment()  {
    private lateinit var binding: ForgotPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = ForgotPasswordBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.buttonSend.setOnClickListener {
            firebaseAuth.sendPasswordResetEmail(binding.editTextEmailSend.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(), "Check on your mail to reset password", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else{
                        Toast.makeText(requireContext(), "Unable to reset Password", Toast.LENGTH_SHORT).show()
                        dismiss()

                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Unable to reset Password", Toast.LENGTH_SHORT).show()

                }
        }

        return binding.root
    }
}