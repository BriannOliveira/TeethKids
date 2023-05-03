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
    private val viewModel: UserRegistrationViewModel by viewModels()
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

            //Armazenamento do currículo no viewModel
            viewModel.updateResume(binding.etCurriculo.text.toString())
            viewModel.uiState.value.resume?.let { Value ->
                if(Value.isEmpty()){
                    val snackbar = Snackbar.make(view,"Preencha o campo Currículo corretamente com pelo menos 5 linhas.", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                } else {
                    //chamar a função de criar a conta...
                    signUpNewAccount(
                        viewModel.uiState.value.name,
                        viewModel.uiState.value.email,
                        viewModel.uiState.value.password,
                        viewModel.uiState.value.phone,
                        viewModel.uiState.value.address1,
                        viewModel.uiState.value.address2,
                        viewModel.uiState.value.address3,
                        viewModel.uiState.value.resume,
                        (activity as MainActivity).getFcmToken()
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
        address1: String,
        address2: String,
        address3: String,
        resume : String,
        fcmToken: String
    ){
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()){ task ->
                if(task.isSuccessful){
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    (activity as MainActivity ).storeUserId(user!!.uid)
                    //atualizar o perfil do usuário com os dados chamando a function.
                    updateUserProfile(name, phone, email, resume, address1, address2, address3, user!!.uid, fcmToken)
                        .addOnCompleteListener(requireActivity()) { res ->
                            //conta criada com sucesso.
                            if(res.result.status == "SUCESS"){
                                hideKeyboard()
                                Snackbar.make(requireView(), "Conta cadastrada com sucesso! Pode fazer o login", Snackbar.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_SignUp3_to_LoginFragment)
                            }
                        }
                } else {
                    Log.w(TAG, "createUserWithEmail: failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed", Toast.LENGTH_SHORT).show()

                    //dar seguimento ao tratamento de erro
                }
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
        uid: String,
        fcmToken: String
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
            "fcmToken" to fcmToken
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