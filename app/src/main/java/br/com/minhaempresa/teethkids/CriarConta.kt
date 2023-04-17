package br.com.minhaempresa.teethkids


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import androidx.navigation.findNavController
import br.com.minhaempresa.teethkids.databinding.ActivityCriarContaBinding
import br.com.minhaempresa.teethkids.databinding.FragmentLoginBinding

class CriarConta : AppCompatActivity() {

    private lateinit var binding: ActivityCriarContaBinding


    //Quando criar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCriarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Intents:
        val intentMain = Intent(this, MainActivity::class.java)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

    }

    private fun cadastrarUsuário(){        //Lógica para cadastrar o usuário no banco de dados
        //Aqui pode chamar um API ou salvar os dados em um banco de dados local
    }
}