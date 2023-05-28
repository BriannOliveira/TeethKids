package br.com.minhaempresa.teethkids.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentNotificationsDisabledBinding
import br.com.minhaempresa.teethkids.menu.emergency.EmergenciesFragment


class NotificationsDisabledFragment : Fragment() {

    private var _binding : FragmentNotificationsDisabledBinding? = null
    private val binding get() = _binding!!
    private lateinit var emergenciesFragment: EmergenciesFragment

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted){
            //não faça nada
        } else {
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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationsDisabledBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAtivar.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else
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