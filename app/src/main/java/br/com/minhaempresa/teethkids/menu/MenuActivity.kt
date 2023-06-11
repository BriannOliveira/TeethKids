package br.com.minhaempresa.teethkids.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.ActivityMenuBinding
import br.com.minhaempresa.teethkids.menu.emergency.EmergenciesFragment
import br.com.minhaempresa.teethkids.menu.profile.ProfileFragment
import br.com.minhaempresa.teethkids.menu.reputation.ReputationFragment
import com.google.firebase.messaging.FirebaseMessaging

class MenuActivity : AppCompatActivity(){
    private var _binding: ActivityMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var emergenciesFragment: EmergenciesFragment
    private lateinit var notificationsfragment: NotificationsDisabledFragment

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted){
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_menu, notificationsfragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {

            //inscrever o dispositivo no tópico new_emergency para receber notificações do FCM
            FirebaseMessaging.getInstance().subscribeToTopic("new_emergency")
                .addOnCompleteListener{ task ->
                    var msg = "Inscrito para o tópico new_emergency."
                    if(!task.isSuccessful){
                        msg = "Inscrição para o tópico new_emergency falhou."
                    }
                    Log.d("FMSubscription",msg)
                }

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_menu, emergenciesFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

     fun askNotificationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_menu, emergenciesFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //instânciando os fragmentos
        emergenciesFragment = EmergenciesFragment()
        notificationsfragment = NotificationsDisabledFragment()

        //pegando dados passados do outro fragment
        val user = intent.getStringExtra("user")
        Log.d("UserExtra","${user}")

        //passando o dado para o menuFragment
        val bundle = Bundle()
        bundle.putString("user",user)
        emergenciesFragment.arguments = bundle

        //configurando o fragmentManager
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container_menu, emergenciesFragment)
        fragmentTransaction.commit()

        //configurando o bottom navigation view
        binding.navView.setOnItemSelectedListener{ item ->
            Log.d("NAV","Item selected ${item.itemId}")
            when(item.itemId){
                R.id.navigation_emergencies -> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.container_menu, emergenciesFragment)
                        .commit()
                    Log.d("ItemID","Id: ${R.id.navigation_emergencies}")
                    true
                }
                R.id.navigation_myreputation -> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    var reputationFragment = ReputationFragment()
                    fragmentTransaction.replace(R.id.container_menu, reputationFragment)
                        .commit()
                    true
                }
                R.id.navigation_profile -> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    var profileFragment = ProfileFragment()
                    fragmentTransaction.replace(R.id.container_menu, profileFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

        //configurando toolbar
        setSupportActionBar(binding.myToolbar)

        //perguntar pela permissão de notificação
        askNotificationPermission();
    }
}