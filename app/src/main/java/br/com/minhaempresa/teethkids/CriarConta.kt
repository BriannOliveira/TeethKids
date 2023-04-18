package br.com.minhaempresa.teethkids


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import br.com.minhaempresa.teethkids.databinding.ActivityCriarContaBinding



class CriarConta : AppCompatActivity() {

    private lateinit var binding: ActivityCriarContaBinding


    //Quando criar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCriarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        navController.navigate(R.id.nav_graph)

    }

    private fun cadastrarUsuário(){        //Lógica para cadastrar o usuário no banco de dados
        //Aqui pode chamar um API ou salvar os dados em um banco de dados local
    }
}