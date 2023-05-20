package br.com.minhaempresa.teethkids.menu.recyclerViewHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.com.minhaempresa.teethkids.databinding.FragmentDetailEmergencyBinding
import br.com.minhaempresa.teethkids.menu.home.HomeViewModel


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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}