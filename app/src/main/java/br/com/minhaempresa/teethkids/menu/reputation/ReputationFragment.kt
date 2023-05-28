package br.com.minhaempresa.teethkids.menu.reputation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentReputationBinding
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.EmergencyAdapter
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.Avaliation
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.AvaliationAdapter
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.avaliationslist


class ReputationFragment : Fragment(), EmergencyAdapter.RecyclerViewEvent {

    private var _binding : FragmentReputationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReputationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buscarDados()

        //configurar o recyclerView
        var recyclerView = binding.rvAvaliations
        var avaliationAdapter = AvaliationAdapter(avaliationslist,this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = avaliationAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buscarDados(){

        val aval1 = Avaliation(
            "20/04",
            4.5,
            "jflksjflkslfjklsjlkf",
            "Briann Oliveira",
        )

        avaliationslist.add(aval1)

        val aval2 = Avaliation(
            "22/04",
            4.8,
            "Muito Bom!",
            "Francisco Cardoso",
        )

        avaliationslist.add(aval2)

    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }

}