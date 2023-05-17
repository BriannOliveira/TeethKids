package br.com.minhaempresa.teethkids.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentHomeBinding
import br.com.minhaempresa.teethkids.ui.recyclerViewHome.DetailEmergencyFragment
import br.com.minhaempresa.teethkids.ui.recyclerViewHome.Emergency
import br.com.minhaempresa.teethkids.ui.recyclerViewHome.EmergencyAdapter
import br.com.minhaempresa.teethkids.ui.recyclerViewHome.EmergencyClickListener
import br.com.minhaempresa.teethkids.ui.recyclerViewHome.emergencyList

class HomeFragment : Fragment(), EmergencyAdapter.RecyclerViewEvent {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        buscarDados()
        val homefragment = this
        binding.rvEmergencias.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = EmergencyAdapter(emergencyList,homefragment)
        }
        return binding.root

    }

    override fun onItemClick(position: Int) {
        //Implementar aqui a codificação para o Fragment do DetailEmergency correspondente ao elemento que clicou
        findNavController().navigate(R.id.action_HomeFragment_to_DetailEmergencyFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMenu.setOnClickListener{
            findNavController().navigate(R.id.action_HomeFragment_to_FragmentMenu)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buscarDados(){
        val emerg1 = Emergency(
            R.drawable.avatar,
            "Briann Oliveira",
            "(19)99406-1793"
        )
        emergencyList.add(emerg1)

        val emerg2 = Emergency(
            R.drawable.avatar,
            "Fernando Gomes",
            "(19)99784-9876"
        )
        emergencyList.add(emerg2)

        val emerg3 = Emergency(
            R.drawable.avatar,
        "Gabriela Souza",
        "(19)99387-3987"
        )
        emergencyList.add(emerg3)
    }



}