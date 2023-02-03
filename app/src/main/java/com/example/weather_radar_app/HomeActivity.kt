package com.example.weather_radar_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather_radar_app.fragments.LifecycleBoundLocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class HomeActivity : AppCompatActivity(),KodeinAware {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView:NavigationView
    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userPhone : TextView
    private lateinit var image : ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var  auth: FirebaseAuth

    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback = object : LocationCallback() {
      /*  override fun onLocationResult(p0: LocationResult) {
            if (p0 != null) {
                super.onLocationResult(p0)
            }
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        navigationView = findViewById(R.id.navigationView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = findNavController(R.id.fragment)
        drawerLayout = findViewById(R.id.drawer)
        navigationView.setupWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        setupActionBarWithNavController(navController,appBarConfiguration)
        fetchUserProfile ()
        val headerView = navigationView.getHeaderView(0)
        userName = headerView.findViewById(R.id.headerName)
        userEmail = headerView.findViewById(R.id.headerEmail)
        userPhone = headerView.findViewById(R.id.headerPhone)
        image = headerView.findViewById(R.id.user_profile_image)

        //logout
        val logout = navigationView.menu.findItem(R.id.logout)
        logout.setOnMenuItemClickListener {
            Toast.makeText(this@HomeActivity, "Signing out", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        //share
        val share = navigationView.menu.findItem(R.id.share)
        share.setOnMenuItemClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Weather Updates")
            startActivity(Intent.createChooser(intent, "Choose One!"))
            true
        }

        requestLocationPermission()
        if (hasLocationPermission()) {
            bindLocationManager()
        }
        else {
            requestLocationPermission()
        }

    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient, locationCallback
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //update header details
    private fun fetchUserProfile (){
        val firebaseUser = auth.currentUser
        val userid =firebaseUser!!.uid
        databaseReference.child(userid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName.text = snapshot.child("name").value.toString()
                userPhone.text = snapshot.child("number").value.toString()
                userEmail.text = snapshot.child("email").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),MY_PERMISSION_ACCESS_COARSE_LOCATION)
    }
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindLocationManager()
            }
            else {
                Toast.makeText(this, "Please, set location manually in settings", Toast.LENGTH_LONG).show()
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        if (resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data)
            setResult(RESULT_OK, data)
        }
    }


}