package br.com.minhaempresa.teethkids.acceptance

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.minhaempresa.teethkids.databinding.FragmentEmergencyDoneBinding
import br.com.minhaempresa.teethkids.menu.MenuActivity


class EmergencyDoneFragment : Fragment() {

    private var _binding: FragmentEmergencyDoneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(requireContext(),MenuActivity::class.java)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(intent)
            requireActivity().finish()
        },3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}