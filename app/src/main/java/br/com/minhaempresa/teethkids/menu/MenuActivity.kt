package br.com.minhaempresa.teethkids.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.ActivityMenuBinding
import br.com.minhaempresa.teethkids.menu.home.HomeFragment
import br.com.minhaempresa.teethkids.menu.recyclerViewHome.DetailEmergencyFragment

class MenuActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _binding: ActivityMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var menuFragment: MenuFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var notificationsfragment: NotificationsDisabledFragment
    private val fragmentManager = supportFragmentManager
    private val fragmentTransaction = fragmentManager.beginTransaction()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted){
            fragmentTransaction.replace(R.id.container_menu, notificationsfragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            fragmentTransaction.replace(R.id.container_menu, menuFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

     fun askNotificationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            fragmentTransaction.replace(R.id.container_menu, menuFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            // FCM SDK (and your app) can post notifications.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            fragmentTransaction.replace(R.id.container_menu, notificationsfragment)
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


        menuFragment = MenuFragment()
        notificationsfragment = NotificationsDisabledFragment()
        homeFragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container_menu, menuFragment)
        fragmentTransaction.commit()


        //perguntar pela permissão de notificação
        askNotificationPermission();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}