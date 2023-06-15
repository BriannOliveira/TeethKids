package br.com.minhaempresa.teethkids.menu

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.ActivityMenuBinding
import br.com.minhaempresa.teethkids.menu.emergency.EmergenciesFragment
import br.com.minhaempresa.teethkids.menu.profile.ProfileFragment
import br.com.minhaempresa.teethkids.menu.reputation.ReputationFragment
import com.google.firebase.messaging.FirebaseMessaging

class MenuActivity : AppCompatActivity(){
    private var _binding: ActivityMenuBinding? = null
    val binding get() = _binding!!
    private lateinit var emergenciesFragment: EmergenciesFragment
    private lateinit var permissionsDeniedFragment: PermissionsDeniedFragment
    private val permissionsrequestCode = 1
    private val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_FINE_LOCATION)

     fun askPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_menu, emergenciesFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            // Directly ask for the permission
            ActivityCompat.requestPermissions(this, permissions, permissionsrequestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //instânciando os fragmentos
        emergenciesFragment = EmergenciesFragment()
        permissionsDeniedFragment = PermissionsDeniedFragment()

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

        //perguntar pela permissão de notificação
        askPermissions()

        //configurando toolbar
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val title = TextView(this)
        title.text = "iTooth"
        val typeface = ResourcesCompat.getFont(this, R.font.alphakind)
        title.typeface = typeface
        title.setTextColor(ContextCompat.getColor(this,R.color.whitetheme))
        title.textSize = 30f
        binding.myToolbar.addView(title)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            permissionsrequestCode -> {
                val allPermissionGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
                if(grantResults.isNotEmpty() && allPermissionGranted){
                    //ir para o fragment
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
                } else {
                    //ir para o fragment de permissões negadas
                    binding.navView.visibility = View.GONE
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.container_menu, permissionsDeniedFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                return
            }
            else -> {
                Unit
            }
        }
    }
}