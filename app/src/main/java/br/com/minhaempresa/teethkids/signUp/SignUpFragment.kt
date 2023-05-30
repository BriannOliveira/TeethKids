package br.com.minhaempresa.teethkids.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import br.com.minhaempresa.teethkids.login.MainActivity
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.Snackbar


class SignUpFragment : Fragment() {


    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                findNavController().navigate(R.id.action_SignUp_to_CameraPreview)
            }else{
                Snackbar.make(binding.root, "Você não concedeu permissão para usar a câmera.", Snackbar.LENGTH_INDEFINITE).show()
            }
        }

    //Variáveis da UI


    //Na inicialização da tela
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Na tela criada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (activity as MainActivity).viewModel

        //Configuração para a imagem ter função para tirar selfie
        binding.imgUsuario.setOnClickListener(){
            cameraProviderResult.launch(android.Manifest.permission.CAMERA)
        }


        //Botão para ir para o segundo fragmento do login
        binding.btnAvancar.setOnClickListener(){

            //Armazenamento dos dados no ViewModel para utilizar em outros fragmentos
            viewModel.updateName(binding.etNome.text.toString())
            viewModel.updateEmail(binding.etEmail.text.toString())
            viewModel.updatePassword(binding.etSenha.text.toString())

            Log.d("Mensagem", "Nome: ${viewModel.uiState.value.name}")
            Log.d("Mensagem", "Email: ${viewModel.uiState.value.email}")
            Log.d("Mensagem", "Senha: ${viewModel.uiState.value.password}")
            Log.d("TAG", "ViewModel hashcode: " + viewModel.hashCode())

            //Declaração de variáveis e transformando o conteúdo das variáveis em String
            val senha = binding.etSenha.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val name = viewModel.uiState.value.name

            //Ação caso o campo do email, senha e nome estiver vazio
            if(email.isEmpty()||senha.isEmpty()){
                val snackbar = Snackbar.make(view,"Preencha o campo do E-mail ou Senha corretamente.", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }
            else if (name.isEmpty() == true){
                val snackbar1 = Snackbar.make(view,"Preencha o campo do Nome corretamente.", Snackbar.LENGTH_SHORT)
                snackbar1.setBackgroundTint(Color.RED)
                snackbar1.show()
            }
            else{
                findNavController().navigate(R.id.action_SignUp_to_SignUp2)
                }
            }

        //Botão voltar para a MainActivity
        binding.btnVoltar.setOnClickListener()
        {
            findNavController().navigate(R.id.action_SignUp_comeBack)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

