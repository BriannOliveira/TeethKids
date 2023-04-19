package br.com.minhaempresa.teethkids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.app.Activity.RESULT_OK
import android.graphics.Color
import br.com.minhaempresa.teethkids.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Configuração para a imagem ter função para tirar selfie
        binding.imgUsuario.setOnClickListener(){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        //Botão para ir para o terceiro fragmento do login
        binding.btnAvancar.setOnClickListener(){

            //Declaração de variáveis e transformando o conteúdo das variáveis em String
            val senha = binding.etSenha.text.toString()
            val email = binding.etEmail.text.toString()

            //Ação caso o campo do email e senha estiver vazio
            if(email.isEmpty()||(senha.isEmpty())){
                val snackbar = Snackbar.make(view,"Preencha o campo email", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            }
            else{
                auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener{cadastro ->
                    if (cadastro.isSuccessful) {
                        findNavController().navigate(R.id.action_LoginFragment_to_LoginFragment2)
                    }
                }.addOnFailureListener{

                }
            }
        }

        //Botão voltar para a MainActivity
        binding.btnVoltar.setOnClickListener()
        {
            findNavController().navigate(R.id.action_LoginFragment_comeBack)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgUsuario.setImageBitmap(imageBitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}
