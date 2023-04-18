package br.com.minhaempresa.teethkids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.databinding.FragmentLogin3Binding

class LoginFragment3 : Fragment() {

    private var _binding: FragmentLogin3Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogin3Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Botão para ir para o fragmento 4 do login
        binding.btnAvancar.setOnClickListener(){
            findNavController().navigate(R.id.action_LoginFragment3_to_LoginFragment4)
        }

        //Botão para ir para o fragmento 2 do login
        binding.btnVoltar.setOnClickListener(){
            findNavController().navigate(R.id.action_LoginFragment3_to_LoginFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}