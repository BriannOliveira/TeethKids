package br.com.minhaempresa.teethkids.menu

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.ActivityMenuBinding
import br.com.minhaempresa.teethkids.menu.home.HomeFragment
import br.com.minhaempresa.teethkids.menu.recyclerViewHome.DetailEmergencyFragment
import br.com.minhaempresa.teethkids.menu.recyclerViewHome.Emergency
import br.com.minhaempresa.teethkids.menu.recyclerViewHome.EmergencyDetail

class MenuActivity : AppCompatActivity(), EmergencyDetail {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _binding: ActivityMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailfragment: DetailEmergencyFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        _binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenu.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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

    override fun onDetailEmergency(e: Emergency) {
        //implementar a chamada de uma nova activity passando a emergencia que foi clicada
        // Carregar um novo fragment com os detalhes ou uma nova activity... Intent.
        // FragmentManager open new fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.drawer_layout,detailfragment)
        transaction.addToBackStack(null)
        transaction.commit()

        // i.putExtra("nome"
        //FragmentTranscation (transacacoa)
        // Fragment (construtor do fragment vc coloca os parametros que ele tem que receber)
        // newInstance do fragmento ler os parametros.
    }
}