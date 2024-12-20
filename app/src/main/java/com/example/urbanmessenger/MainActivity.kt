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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_MESSAGES
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.data.network.initFirebase
import com.example.urbanmessenger.data.network.initUser
import com.example.urbanmessenger.databinding.ActivityMainBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.notifications.FCMApi
import com.example.urbanmessenger.notifications.MyFirebaseMessagingService
import com.example.urbanmessenger.notifications.NotificationBody
import com.example.urbanmessenger.notifications.SendMessageDto
import com.example.urbanmessenger.notifications.getNewToken
import com.example.urbanmessenger.notifications.initFirebaseCloudMessaging
import com.example.urbanmessenger.utilits.APP_ACTIVITY
import com.example.urbanmessenger.utilits.AppChildEventListenerNewMessage
import com.example.urbanmessenger.utilits.AppStates
import com.example.urbanmessenger.utilits.AppValueEventListener
import com.example.urbanmessenger.utilits.myToast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import kotlin.collections.listOf

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var headerImage: ImageView? = null
    private var headerFullNameOrEmail: TextView? = null
    private var headerPhoneOrStatus: TextView? = null

    private lateinit var mListenerHeader: AppValueEventListener
    private lateinit var mListenerMessage: AppValueEventListener
    private lateinit var mListenerNewMessage: AppChildEventListenerNewMessage
    private lateinit var mReceivingUser: UserData
    private lateinit var mReceivingMessage: UserData
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefNewMessage: DatabaseReference

    private var notification = MyFirebaseMessagingService()


    private var mOldList = listOf<UserData>()
    private var mNewList = listOf<UserData>()
    private var newMessage = UserData()

    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainActivityToolbar)

        initListeners()


//        initHeadersViews()
//        mListenerHeader = AppValueEventListener {
//            mReceivingUser = it.getUserDataModel()
//            initHeader()
//        }
//
//        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(UID)
//        mRefUser.addValueEventListener(mListenerHeader)


    }

    private fun initListeners() {

//        mListenerHeader = AppValueEventListener {
//            mReceivingUser = it.getUserDataModel()
//        }
//        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(UID)
//        mRefUser.addValueEventListener(mListenerHeader)


        mRefNewMessage = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID)
        mListenerNewMessage = AppChildEventListenerNewMessage({ snapshot -> childAdded(snapshot) },
            { snapshot2 -> childChanged(snapshot2) })
        mRefNewMessage.addChildEventListener(mListenerNewMessage)


    }

    private fun childAdded(snapshot: DataSnapshot) {
        mOldList = snapshot.children.map { it.getUserDataModel() }
        Log.d("@@@", "Child Added")
    }

    private fun childChanged(snapshot: DataSnapshot) {

        mNewList = snapshot.children.map { it.getUserDataModel() }
        for (message in mNewList) {
            if (!mOldList.contains(message)) {
                newMessage = message
                mOldList = mNewList
            }
        }
        //тут скорее всего не тот url указан в логах такая ошибка вылетает
        //Ошибка при отправке: CLEARTEXT communication to 10.0.2.2 not permitted by network security policy
        val fcmApi: FCMApi =
            Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(
                MoshiConverterFactory.create()
            ).build().create()
        val token = getNewToken()
        Log.d("@@@", "Token ${token}")
        val body = SendMessageDto(
            to = token,
            notification = NotificationBody(title = "Новое сообщение", body = newMessage.text)
        )
        Log.d("@@@", "Body ${body}")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                fcmApi.sendMessage(body)
                Log.d("@@@", "FCM API ${fcmApi.sendMessage(body)}")
            } catch (e: Exception) {
                Log.e("@@@", "Ошибка при отправке: ${e.message.toString()}")
            }
        }
//        notification.generateNotification(
//            newMessage.from.toString(),
//            newMessage.text.toString(),
//            this@MainActivity
//        )
        Log.d("@@@", "NewMessage ${newMessage}")

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                mNewList = snapshot.children.map { it.getUserDataModel() }
//                for (message in mNewList) {
//                    if (!mOldList.contains(message)) {
//                        newMessage = message
//                        mOldList = mNewList
//                    }
//                }
//                notification.generateNotification(
//                    newMessage.from.toString(),
//                    newMessage.text.toString(),
//                    this@MainActivity
//                )
//                Log.d("@@@", "NewMessage ${newMessage}")
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                myToast("Оно необходимо для уведомлений")
//            } else {
//                permissionLauncherSingle.launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }


        Log.d("@@@", "${newMessage.text}")
    }

    override fun onResume() {
        super.onResume()

        initNavigation()
        initFirebase()
        initUser()
        initFirebaseCloudMessaging()
//        askNotificationsPermission()

    }

    override fun onPause() {
        super.onPause()
//        mRefUser.removeEventListener(mListenerHeader)
        mRefNewMessage.removeEventListener(mListenerNewMessage)
    }

    private fun askNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                myToast("Разрешение на уведомления получено")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                myToast("Оно необходимо для уведомления")
            } else {
                permissionLauncherSingle.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initHeadersViews() {
        headerImage = findViewById<ImageView>(R.id.headerProfileImage)
        headerPhoneOrStatus = findViewById<TextView>(R.id.headerNumberPhoneProfile)
        headerFullNameOrEmail = findViewById<TextView>(R.id.headerFullNameProfile)
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


    @SuppressLint("SetTextI18n")
    private fun initHeader() {
        initHeadersViews()

        if (mReceivingUser.userPhotoUri != null) {
            headerImage?.setImageURI(mReceivingUser.userPhotoUri.toUri())
        } else {
            headerImage?.setImageResource(R.drawable.man_one)
        }

        if (mReceivingUser.phone.isEmpty()) {
            headerPhoneOrStatus?.text = mReceivingUser.state
        } else {
            headerPhoneOrStatus?.text = mReceivingUser.phone
        }
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

//    private fun setValueEventListener() {
//        mListenerHeader = AppValueEventListener {
//            mReceivingUser = it.getUserDataModel()
//        }
//
//        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(UID)
//        mRefUser.addValueEventListener(mListenerHeader)
//    }

    private fun newMessageListener() {

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


    private fun initNotifications() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("messages")

        databaseRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                val messageText = snapshot.child("text").getValue(String::class.java) ?: return

                val token = getNewToken()
                val body = SendMessageDto(
                    to = token,
                    notification = NotificationBody(
                        title = "Новое сообщение",
                        body = messageText
                    )
                )

                val sendMessage = object : FCMApi {
                    override suspend fun sendMessage(body: SendMessageDto) {}
                    override suspend fun broadcast(body: SendMessageDto) {}
                }

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        sendMessage.sendMessage(body)
                    } catch (e: Exception) {
                        Log.d("@@@", "Ошибка отправки: ${e.message}")
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}

