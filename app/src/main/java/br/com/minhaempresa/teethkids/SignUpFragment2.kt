package br.com.minhaempresa.teethkids


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.databinding.FragmentSignup2Binding
import br.com.minhaempresa.teethkids.datastore.UserRegistrationViewModel
import com.google.android.material.snackbar.Snackbar


class SignUpFragment2 : Fragment() {

    private val viewModel: UserRegistrationViewModel by viewModels()
    private var _binding: FragmentSignup2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignup2Binding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (activity as MainActivity).viewModel


        //Botão para ir para o segundo fragment
       binding.btnAvancar.setOnClickListener()
        {
            //Verificando se os campos não estão vazios
            if (binding.etTelefone.text.toString().isEmpty() ||
                binding.etEndereco1.text.toString().isEmpty() ||
                binding.etEndereco2.text.toString().isEmpty() ||
                binding.etEndereco3.text.toString().isEmpty()) {
                val snackbar = Snackbar.make(view,"Preencha os campos corretamente.", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else{
                //Armazenando os dados no viewModel
                viewModel.updatePhone(binding.etTelefone.text.toString())
                viewModel.updateAddressOne(binding.etEndereco1.text.toString())
                viewModel.updateAddressTwo(binding.etEndereco2.text.toString())
                viewModel.updateAddressThree(binding.etEndereco3.text.toString())

                Log.d("Mensagem", "Telefone: ${viewModel.uiState.value.phone}")
                Log.d("Mensagem", "Endereço 1: ${viewModel.uiState.value.addressone}")
                Log.d("Mensagem", "Endereço 2: ${viewModel.uiState.value.addresstwo}")
                Log.d("Mensagem", "Endereço 3: ${viewModel.uiState.value.addressthree}")
                Log.d("TAG", "ViewModel hashcode: " + viewModel.hashCode())

                //Próximo fragment
                findNavController().navigate(R.id.action_SignUp2_to_SignUp3)
            }
        }

        //Botão para ir para o primeiro fragmento do login
        binding.btnVoltar.setOnClickListener(){
            findNavController().navigate(R.id.action_SignUp2_to_SignUp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
