package br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.acceptance.AcceptanceActivity
import br.com.minhaempresa.teethkids.acceptance.WaitFragment
import br.com.minhaempresa.teethkids.databinding.FragmentDetailEmergencyBinding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import br.com.minhaempresa.teethkids.menu.MenuActivity
import br.com.minhaempresa.teethkids.menu.emergency.EmergenciesFragment
import com.google.firebase.firestore.FirebaseFirestore


class DetailEmergencyFragment : Fragment() {

    private var _binding: FragmentDetailEmergencyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailEmergencyBinding.inflate(inflater, container, false)
        return(binding.root)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseMyUser.getCurrentUser()
        //Pegar os dados do item selecionado na RecyclerView e setar eles aqui...
        val emergency = arguments?.getParcelable("emergency", Emergency::class.java)

        if (emergency!=null){
            binding.tvEmergencyName.text = emergency.name
            binding.tvEmergencyPhone.text = emergency.phoneNumber
        }

        binding.btnAceitar.setOnClickListener {
            if (currentUser != null){
                FirebaseFirestore.getInstance().collection("emergencies").document(emergency!!.uid)
                    .update("usersaccepted",currentUser.uid)
                val intent = Intent(requireContext(), AcceptanceActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.btnFechar.setOnClickListener {
            val fragmentMenu = EmergenciesFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_menu, fragmentMenu)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}