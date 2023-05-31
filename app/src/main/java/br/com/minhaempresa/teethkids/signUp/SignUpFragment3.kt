package br.com.minhaempresa.teethkids.signUp


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentSignup3Binding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import br.com.minhaempresa.teethkids.login.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment3 : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentSignup3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignup3Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Esse botão termina o ciclo do SignUp e volta para atividade de Login **/
        binding.btnAvancar.setOnClickListener{
            val viewModel = (activity as MainActivity).viewModel
            //Armazenamento do currículo no viewModel
            viewModel.updateResume(binding.etCurriculo.text.toString())
            viewModel.uiState.value.resume.let { Value ->
                if(Value.isEmpty()){
                    val snackbar = Snackbar.make(view,"Preencha o campo Currículo corretamente com pelo menos 5 linhas.", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                } else {
                    Log.d("Mensagem", "Nome: ${viewModel.uiState.value.name}")
                    Log.d("Mensagem", "Email: ${viewModel.uiState.value.email}")
                    Log.d("Mensagem", "Senha: ${viewModel.uiState.value.password}")
                    Log.d("Mensagem", "Telefone: ${viewModel.uiState.value.phone}")
                    Log.d("Mensagem", "Endereço 1: ${viewModel.uiState.value.addressone}")
                    Log.d("Mensagem", "Endereço 2: ${viewModel.uiState.value.addresstwo}")
                    Log.d("Mensagem", "Endereço 3: ${viewModel.uiState.value.addressthree}")
                    Log.d("Mensagem", "Currículo: ${viewModel.uiState.value.resume}")
                    Log.d("TAG", "ViewModel hashcode: " + viewModel.hashCode())

                    //chamar a função de criar a conta...
                    signUpNewAccount(
                        viewModel.uiState.value.name,
                        viewModel.uiState.value.email,
                        viewModel.uiState.value.password,
                        viewModel.uiState.value.phone,
                        viewModel.uiState.value.addressone,
                        viewModel.uiState.value.addresstwo,
                        viewModel.uiState.value.addressthree,
                        viewModel.uiState.value.resume,
                        (activity as MainActivity).getFcmToken() //Não está funcionando
                    )
                }
            }
        }

        //Botão para ir para o fragmento 2 do login
        binding.btnVoltar.setOnClickListener{
            findNavController().navigate(R.id.action_SignUp3_to_SignUp2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signUpNewAccount(
        name: String,
        email: String,
        password: String,
        phone: String,
        addressone: String,
        addresstwo: String,
        addressthree: String,
        resume : String,
        fcmToken: String
    ){
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(requireContext(),"Conta autenticada com sucesso!",Toast.LENGTH_SHORT).show()
            val userMap = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "addressone" to addressone,
                "addresstwo" to addresstwo,
                "addressthree" to addressthree,
                "resume" to resume,
                "fcmToken" to fcmToken,
                "status" to false
            )
            FirebaseMyUser.updateUserName(name)
            val db = (activity as MainActivity).db
            if(FirebaseAuth.getInstance().currentUser!=null){
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                db.collection("user").document(userId).set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(),"Conta cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_SignUp3_to_LoginFragment)
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireContext(), "Falha ao cadastrar sua conta! Tente novamente.", Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(requireContext(), "Current user is null", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(requireContext(), "Falha ao fazer a autenticação! Tente novamente.", Toast.LENGTH_SHORT).show()
        }
    }
}