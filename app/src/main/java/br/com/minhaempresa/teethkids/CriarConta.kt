package br.com.minhaempresa.teethkids

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import androidx.lifecycle.ViewModel


/*class ResgisterViewModel : ViewModel()
{
    var nome: String = ""
    var email: String = ""
    var telefone: String = ""
}*/

class CriarConta : AppCompatActivity() {

    //private val RegisterVM: ResgisterViewModel = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)

        //Declaração de variáveis
        val btnAvancar: Button = findViewById(R.id.btnAvancar)
        val btnVoltar: Button = findViewById(R.id.btnVoltar)
        val intentMain = Intent(this, MainActivity::class.java)
        val intentCriarConta2 = Intent(this, CriarConta2::class.java)

        //Inserindo as informações salvas no campos EditText
        var etNome : EditText = findViewById(R.id.etNome)
        var etEmail: EditText = findViewById(R.id.etEmail)
        var etTelefone: EditText = findViewById(R.id.etTelefone)


        //Função para avançar a tela
        btnAvancar.setOnClickListener()
        {
            startActivity(intentCriarConta2)
            finish()
        }

        //Voltar para a tela inicial
        btnVoltar.setOnClickListener()
        {
            startActivity(intentMain)
            finish()
        }


    }
}