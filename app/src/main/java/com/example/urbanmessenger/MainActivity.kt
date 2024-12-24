package com.example.urbanmessenger

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_MESSAGES
import com.example.urbanmessenger.data.network.NODE_TOKEN
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.getNewToken
import com.example.urbanmessenger.data.network.getTokenDataModel
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.data.network.initFirebase
import com.example.urbanmessenger.data.network.initUser
import com.example.urbanmessenger.data.network.updateToken
import com.example.urbanmessenger.databinding.ActivityMainBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.notifications.APIService
import com.example.urbanmessenger.notifications.Client
import com.example.urbanmessenger.notifications.Data
import com.example.urbanmessenger.notifications.MyResponse
import com.example.urbanmessenger.notifications.NotificationSender
import com.example.urbanmessenger.notifications.Token
import com.example.urbanmessenger.utilits.APP_ACTIVITY
import com.example.urbanmessenger.utilits.AppChildEventListenerNewMessage
import com.example.urbanmessenger.utilits.AppStates
import com.example.urbanmessenger.utilits.AppValueEventListener
import com.example.urbanmessenger.utilits.myToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.listOf

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var headerImage: ImageView? = null
    private var headerFullNameOrEmail: TextView? = null
    private var headerPhoneOrStatus: TextView? = null

    private lateinit var userToken: Token

    private lateinit var mListenerHeader: AppValueEventListener
    private lateinit var mListenerMessage: AppValueEventListener
    private lateinit var mListenerNewMessage: AppChildEventListenerNewMessage
    private lateinit var mReceivingUser: UserData
    private lateinit var mReceivingMessage: UserData
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefNewMessage: DatabaseReference

    private lateinit var apiService: APIService


    private var mOldList = listOf<UserData>()
    private var mNewList = listOf<UserData>()
    private var newMessage = UserData()

    lateinit var navController: NavController


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainActivityToolbar)

        permissionLauncherSingle.launch(Manifest.permission.POST_NOTIFICATIONS)

        initListeners()

        mListenerHeader = AppValueEventListener {
            mReceivingUser = it.getUserDataModel()
            initHeader()
        }
        apiService =
            Client.getClient("https://fcm.googleapis.com").create(APIService::class.java)

        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(UID)
        mRefUser.addValueEventListener(mListenerHeader)

        refreshToken()

    }

    private fun initListeners() {

        mListenerHeader = AppValueEventListener {
            mReceivingUser = it.getUserDataModel()
        }
        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(UID)
        mRefUser.addValueEventListener(mListenerHeader)


        mRefNewMessage = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID)
        mListenerNewMessage = AppChildEventListenerNewMessage({ snapshot -> childAdded(snapshot) },
            { snapshot2 -> childChanged(snapshot2) })
        mRefNewMessage.addChildEventListener(mListenerNewMessage)


    }

    private fun refreshToken() {
        getNewToken()
        DATA_BASE_ROOT.child(NODE_TOKEN).child(UID).addListenerForSingleValueEvent(
            AppValueEventListener { snapshot ->
                userToken = snapshot.getTokenDataModel()
                var token: Token = Token(userToken.token)
                updateToken(token.token)
            })
    }

    private fun childAdded(snapshot: DataSnapshot) {
        mOldList = snapshot.children.map { it.getUserDataModel() }
    }

    private fun childChanged(snapshot: DataSnapshot) {

        mNewList = snapshot.children.map { it.getUserDataModel() }
        for (message in mNewList) {
            if (!mOldList.contains(message)) {
                newMessage = message
                mOldList = mNewList
            }
        }
        Log.d("@@@","pushup send ")
        sendPushUp()

    }

    private fun sendPushUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                sendNotification(userToken.token, "Title", "New message")
                Log.d("@@@","senotification ${sendNotification(userToken.token,"title","newMessage")}")
                updateToken(userToken.token)
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                myToast("Оно необходимо для уведомления")
            } else {
                permissionLauncherSingle.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    override fun onResume() {
        super.onResume()

        initNavigation()
        initFirebase()
        initUser()

    }

    override fun onPause() {
        super.onPause()
        mRefUser.removeEventListener(mListenerHeader)
        mRefNewMessage.removeEventListener(mListenerNewMessage)
    }


    private val permissionLauncherSingle = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            myToast("Разрешение на уведомления получено")
        } else {
            myToast("Разрешение на уведомления не получено")
        }

    }

    private fun sendNotification(userToken: String, title: String, message: String) {
        var data = Data(title, message)
        var sender: NotificationSender = NotificationSender(data, userToken)
        apiService.sendNotification(sender)!!.enqueue(object : Callback<MyResponse?> {
            override fun onResponse(
                call: Call<MyResponse?>,
                response: Response<MyResponse?>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.success != 1) {
                        myToast("Failed")
                    }
                }
            }

            override fun onFailure(
                call: Call<MyResponse?>,
                t: Throwable
            ) {
            }

        })

    }


    @SuppressLint("SetTextI18n")
    private fun initHeader() {
        headerImage = findViewById<ImageView>(R.id.headerProfileImage)
        headerPhoneOrStatus = findViewById<TextView>(R.id.headerNumberPhoneProfile)
        headerFullNameOrEmail = findViewById<TextView>(R.id.headerFullNameProfile)

        if (mReceivingUser.userPhotoUri != null) {
            Glide.with(this).load(mReceivingUser.userPhotoUri).into(headerImage!!)
        } else {
            headerImage?.setImageResource(R.drawable.man_one)
        }


        headerPhoneOrStatus?.text = mReceivingUser.state ?: mReceivingUser.phone

        if (mReceivingUser.firstname.isEmpty() && mReceivingUser.lastname.isEmpty()) {
            headerFullNameOrEmail?.text = mReceivingUser.email
        } else headerFullNameOrEmail?.text =
            "${mReceivingUser.firstname} ${mReceivingUser.lastname}"

    }

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY = this
        AppStates.updateState(AppStates.ONLINE, this)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_log_out_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOutItem -> {
                AUTHFIREBASE.signOut()
                finish()
                startActivity(Intent(this, AuthActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

