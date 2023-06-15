package br.com.minhaempresa.teethkids.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.ActivityMainBinding
import br.com.minhaempresa.teethkids.signUp.model.UserRegistrationViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val db = FirebaseFirestore.getInstance()
    val viewModel: UserRegistrationViewModel by viewModels()

    private fun prepareFirebaseAppCheckDebug(){
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
    }

    private fun storeFcmToken() {
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            //guardar esse token
            viewModel.updateFcmToken(task.result)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        navController.navigate(R.id.nav_graph)

        FirebaseApp.initializeApp(this)

        // disponibilizando o token (que deve ser colocado lá no APP CHECK do Firebase).
        prepareFirebaseAppCheckDebug()

        // guardar o token FCM pois iremos precisar.
        storeFcmToken()
    }
}