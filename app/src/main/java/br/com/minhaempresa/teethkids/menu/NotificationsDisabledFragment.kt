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
import br.com.minhaempresa.teethkids.databinding.FragmentNotificationsDisabledBinding
import br.com.minhaempresa.teethkids.menu.emergency.EmergenciesFragment
import com.google.firebase.messaging.FirebaseMessaging


class NotificationsDisabledFragment : Fragment() {

    private var _binding : FragmentNotificationsDisabledBinding? = null
    private val binding get() = _binding!!
    private lateinit var emergenciesFragment: EmergenciesFragment

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted){
            Toast.makeText(requireContext(),"Não vai ser possível usar este aplicativo. Caso queira usar, reinstale o aplicativo.",Toast.LENGTH_LONG).show()
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

            emergenciesFragment = EmergenciesFragment()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container_menu, emergenciesFragment)
                commit()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationsDisabledBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAtivar.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_DENIED)
            {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}