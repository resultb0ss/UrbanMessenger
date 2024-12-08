package com.example.urbanmessenger

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.data.network.initFirebase
import com.example.urbanmessenger.data.network.initUser
import com.example.urbanmessenger.databinding.ActivityMainBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utilits.APP_ACTIVITY
import com.example.urbanmessenger.utilits.AppStates
import com.example.urbanmessenger.utilits.AppValueEventListener
import com.example.urbanmessenger.utilits.myToast
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var headerImage: ImageView
    private lateinit var headerFullNameOrEmail: TextView
    private lateinit var headerPhoneOrStatus: TextView

    private lateinit var mListenerHeader: AppValueEventListener
    private lateinit var mReceivingUser: UserData
    private lateinit var mRefUser: DatabaseReference

    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )


    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainActivityToolbar)
        permissionLauncherMultiple.launch(permissions)


    }

    override fun onResume() {
        super.onResume()

        initNavigation()
        initFirebase()
        initUser()
        setValueEventListener()

    }

    override fun onPause() {
        super.onPause()
        mRefUser.removeEventListener(mListenerHeader)
    }

    private val permissionLauncherMultiple = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        var allAreGranted = true
        for (isGranted in result.values) {
            allAreGranted = allAreGranted && isGranted
            {
                if (isGranted) {
                    myToast("Не все разрешения получены не получено")
                } else {
                    myToast("Все разрешения получены не получено")
                }
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun initHeadersFields() {
        headerImage = findViewById<ImageView>(R.id.headerProfileImage)
        headerPhoneOrStatus = findViewById<TextView>(R.id.headerNumberPhoneProfile)
        headerFullNameOrEmail = findViewById<TextView>(R.id.headerFullNameProfile)

        if (mReceivingUser.userPhotoUri != null) {
            headerImage.setImageURI(mReceivingUser.userPhotoUri.toUri())
        } else {
            headerImage.setImageResource(R.drawable.man_one)
        }

        if (mReceivingUser.phone.isEmpty()) {
            headerPhoneOrStatus.text = mReceivingUser.state
        } else {
            headerPhoneOrStatus.text = mReceivingUser.phone
        }
        if (mReceivingUser.firstname.isEmpty() && mReceivingUser.lastname.isEmpty()) {
            headerFullNameOrEmail.text = mReceivingUser.email
        } else headerFullNameOrEmail.text = "${mReceivingUser.firstname} ${mReceivingUser.lastname}"

    }

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY = this
        AppStates.updateState(AppStates.ONLINE, this)

    }

    private fun setValueEventListener() {
        mListenerHeader = AppValueEventListener {
            mReceivingUser = it.getUserDataModel()
//            initHeadersFields()
        }

        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(UID)
        mRefUser.addValueEventListener(mListenerHeader)
    }


    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        val builder = AppBarConfiguration.Builder(navController.graph)
        builder.setOpenableLayout(binding.drawerLayout)
        val appBarConfiguration = builder.build()
        binding.mainActivityToolbar.setupWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateToolbarTitle(newTitle: String) {
        binding.mainActivityToolbar.setTitle(newTitle)
    }


}

