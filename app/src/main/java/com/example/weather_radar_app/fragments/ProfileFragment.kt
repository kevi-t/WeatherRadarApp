package com.example.weather_radar_app.fragments


import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.weather_radar_app.User
import com.example.weather_radar_app.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var  storageReference: StorageReference
    private lateinit var  imageUri: Uri
    private lateinit var  intent:Intent
    private lateinit var  profileName: String
    private lateinit var  profileEmail: String
    private lateinit var  profileCity: String
    private lateinit var  profileNumber: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        showUsersProfile()
        
        binding.buttonAddPhoto.setOnClickListener(View.OnClickListener {
                showPictureDialog()
        })
        binding.buttonUpdate.setOnClickListener{
            val fullName = binding.editTextTextPersonName.text.toString()
            val textEmail = binding.editTextTextEmailAddress.text.toString()
            val phoneNumber = binding.editTextPhone2.text.toString()
            val currentLocation= binding.editTextTextPersonName2.text.toString()

            val user = User(fullName,textEmail,phoneNumber,currentLocation)
            if (uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful){
                        uploadProfilePicture()
                    }
                    else{
                        Toast.makeText(requireContext(),"Failed to update Profile",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return binding.root
    }

    private fun showUsersProfile() {

        profileName = requireActivity().intent.getStringExtra("name").toString()
        profileEmail = requireActivity().intent.getStringExtra("email").toString()
        profileNumber = requireActivity().intent.getStringExtra("number").toString()
        profileCity = requireActivity().intent.getStringExtra("city").toString()

        binding.editTextTextPersonName.setText(profileName)
        binding.editTextTextEmailAddress.setText(profileEmail)
        binding.editTextPhone2.setText(profileNumber)
        binding.editTextTextPersonName2.setText(profileCity)


    }

    private fun uploadProfilePicture() {

       imageUri = Uri.parse("android.resource//com.example.weather_radar_app/drawable/avatar)")
       storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid)
       storageReference.putFile(imageUri).addOnSuccessListener{
           Toast.makeText(requireContext(),"Profile updated successfully",Toast.LENGTH_SHORT).show()
       }.addOnFailureListener{
           Toast.makeText(requireContext(),"Image upload failed",Toast.LENGTH_SHORT).show()
       }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery","Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> //                    choosePhotoFromGallary();
                    choosePhotoFromGallery()
                1 -> //                    takePhotoFromCamera();
                    takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val resultUri: Uri? = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,ContentValues())
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, 22)
        }
    }
    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 11)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                if (data != null) {
                    val contentURI: Uri? = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,contentURI)
                        //                    String path = saveImage(bitmap);
                        //                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                        binding.profileImage.setImageBitmap(bitmap)
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
                run { Toast.makeText(activity, "Data not found", Toast.LENGTH_SHORT).show() }
            }
        }
    }


}