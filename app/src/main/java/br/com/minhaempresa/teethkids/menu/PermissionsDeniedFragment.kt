package br.com.minhaempresa.teethkids.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentPermissionDeniedBinding
import br.com.minhaempresa.teethkids.menu.emergency.EmergenciesFragment
import com.google.firebase.messaging.FirebaseMessaging


class PermissionsDeniedFragment : Fragment() {

    private var _binding : FragmentPermissionDeniedBinding? = null
    private val binding get() = _binding!!
    private lateinit var emergenciesFragment: EmergenciesFragment


    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all{
            it.value == true
        }
        if (granted){
            FirebaseMessaging.getInstance().subscribeToTopic("new_emergency")
                .addOnCompleteListener{ task ->
                    var msg = "Inscrito para o tópico new_emergency."
                    if(!task.isSuccessful){
                        msg = "Inscrição para o tópico new_emergency falhou."
                    }
                    Log.d("FMSubscription",msg)
                }
            emergenciesFragment = EmergenciesFragment()
            (activity as MenuActivity).binding.navView.visibility = View.VISIBLE
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container_menu, emergenciesFragment)
                commit()
            }
        } else {
            Toast.makeText(requireContext(),"Não vai ser possível usar este aplicativo. Caso queira usar, por favor reinstale o aplicativo.",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPermissionDeniedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //permissões
        val checkLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val checkNotficationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)

        //requerir permissões
        binding.fabAtivar.setOnClickListener {
            val permissionToRequest = mutableListOf<String>()
            if (checkNotficationPermission == PackageManager.PERMISSION_DENIED)
            {
                permissionToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
            if (checkLocationPermission == PackageManager.PERMISSION_DENIED)
            {
                permissionToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if(permissionToRequest.isNotEmpty()){
                requestMultiplePermissionsLauncher.launch(permissionToRequest.toTypedArray())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}