package br.com.minhaempresa.teethkids

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.databinding.FragmentSignup3Binding
import br.com.minhaempresa.teethkids.datastore.UserRegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.functions.ktx.functions
import org.json.JSONArray
import org.json.JSONObject

class SignUpFragment3 : Fragment() {

    private val TAG = "SignUpFragment"
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    private var _binding: FragmentSignup3Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignup3Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /** Esse botão termina o ciclo do SignUp e volta para atividade de Login **/
        binding.btnAvancar.setOnClickListener(){
            val viewModel = (activity as MainActivity).viewModel
            //Armazenamento do currículo no viewModel
            viewModel.updateResume(binding.etCurriculo.text.toString())
            viewModel.uiState.value.resume?.let { Value ->
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
                        viewModel.uiState.value.resume
                    )
                }
            }
        }

        //Botão para ir para o fragmento 2 do login
        binding.btnVoltar.setOnClickListener(){
            findNavController().navigate(R.id.action_SignUp3_to_SignUp2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }

    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun signUpNewAccount(
        name: String,
        email: String,
        password: String,
        phone: String,
        addressone: String,
        addresstwo: String,
        addressthree: String,
        resume : String
    ){
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { cadastro ->
            Toast.makeText(requireContext(),"Conta autenticada com sucesso!",Toast.LENGTH_SHORT).show()
            val userMap = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "addressone" to addressone,
                "addresstwo" to addresstwo,
                "addressthree" to addressthree,
                "resume" to resume,
            )

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

    private fun updateUserProfile(
        name: String,
        phone: String,
        email: String,
        resume: String,
        address1: String,
        address2: String,
        address3: String,
        uid: String
    ) : Task<CustomResponse> {
        //chamar a functions para atualizar o perfil
        functions = Firebase.functions("southamerica-east1")

        //Create the arguments to the callable function
        val data = hashMapOf(
            "nome" to name,
            "telefone" to phone,
            "email" to email,
            "currículo" to resume,
            "endereço 1" to address1,
            "endereço 2" to address2,
            "endereço 3" to address3,
            "uid" to uid,
        )

        return functions
            .getHttpsCallable("setUserProfile")
            .call(data)
            .continueWith { task ->
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}