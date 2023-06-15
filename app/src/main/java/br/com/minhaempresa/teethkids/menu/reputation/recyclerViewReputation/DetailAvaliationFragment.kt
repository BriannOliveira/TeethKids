package br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentDetailAvaliationBinding
import br.com.minhaempresa.teethkids.databinding.FragmentDetailEmergencyBinding
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Emergency
import br.com.minhaempresa.teethkids.menu.reputation.ReputationFragment

class DetailAvaliationFragment : Fragment() {

    private var _binding: FragmentDetailAvaliationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailAvaliationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Pegar a avaliation como argumento
        val avaliation = arguments?.getParcelable("avaliation", Avaliation::class.java)

        if(avaliation != null){
            binding.tvAvaliationName.text = avaliation.name
            binding.tvAvaliationComment.text = avaliation.comment
            binding.rbAvaliation.rating = avaliation.rate.toFloat()
        }

        binding.btnVoltar.setOnClickListener {

            val reputationFragment = ReputationFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_menu, reputationFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}