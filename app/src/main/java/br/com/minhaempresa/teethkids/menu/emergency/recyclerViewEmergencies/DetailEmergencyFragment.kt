package br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentDetailEmergencyBinding
import br.com.minhaempresa.teethkids.menu.emergency.EmergenciesFragment


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

        //Pegar os dados do item selecionado na RecyclerView e setar eles aqui...
        val emergency = arguments?.getParcelable("emergency", Emergency::class.java)
        if (emergency!=null){
            binding.tvEmergencyName.text = emergency.nameUser
            binding.tvEmergencyPhone.text = emergency.phone
        }

        binding.imgFechar.setOnClickListener {
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