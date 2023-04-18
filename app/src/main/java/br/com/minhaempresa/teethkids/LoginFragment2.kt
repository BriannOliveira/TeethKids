package br.com.minhaempresa.teethkids


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.databinding.FragmentLogin2Binding


class LoginFragment2 : Fragment() {

    private var _binding: FragmentLogin2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogin2Binding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Botão para ir para o segundo fragment
        binding.btnAvancar.setOnClickListener()
        {
            findNavController().navigate(R.id.action_LoginFragment2_to_LoginFragment3)
        }

        //Botão para ir para o primeiro fragmento do login
        binding.btnVoltar.setOnClickListener(){
            findNavController().navigate(R.id.action_LoginFragment2_to_LoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
