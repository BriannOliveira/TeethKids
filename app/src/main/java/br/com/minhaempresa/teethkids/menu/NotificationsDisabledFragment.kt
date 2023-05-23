package br.com.minhaempresa.teethkids.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentNotificationsDisabledBinding


class NotificationsDisabledFragment : Fragment() {

    private var _binding : FragmentNotificationsDisabledBinding? = null
    private val binding get() = _binding!!


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
            val menu = (activity as MenuActivity)
            menu.askNotificationPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}