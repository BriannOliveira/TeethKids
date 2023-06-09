package br.com.minhaempresa.teethkids.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentLoginBinding
import br.com.minhaempresa.teethkids.menu.MenuActivity
import br.com.minhaempresa.teethkids.signUp.SignUpFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null
    private val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener{
            //verificar nulidade
            if(binding.etEmail.text!!.isNotEmpty() && binding.etSenha.text!!.isNotEmpty()){
                Log.d("email",binding.etEmail.text.toString())
                Log.d("senha", binding.etSenha.text.toString())
                login(binding.etEmail.text.toString(),binding.etSenha.text.toString())
            } else {
                Toast.makeText(requireContext(),"Por favor, preencha os campos corretamente.",Toast.LENGTH_LONG).show()
            }
        }

        //criar conta nova
        binding.btnCriarconta.setOnClickListener{
            val signUpFragment = SignUpFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, signUpFragment)
                .commit()
        }
    }

    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun login(email: String, password: String){
        hideKeyboard()
        // inicializando o auth.
        auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // login completado com sucesso.
                    val user = auth.currentUser?.uid
                    if(user != null){
                        val intent = Intent(requireContext(), MenuActivity::class.java)
                        intent.putExtra("user",user)
                        Log.d("UserRoot","$user")
                        startActivity(intent)
                        (activity as MainActivity).finish()
                    } else {
                        Toast.makeText(requireContext(),"Ocorreu um erro ao fazer o login.",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (it.exception is FirebaseAuthException) {
                        Snackbar.make(requireView(),"Não foi possível fazer o login, verifique os dados e tente novamente.", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
    }
}