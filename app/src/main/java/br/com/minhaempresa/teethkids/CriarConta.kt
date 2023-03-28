package br.com.minhaempresa.teethkids


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText

class CriarConta : AppCompatActivity() {
    private var nome: String = ""
    private var email: String = ""
    private var telefone: String = ""

    //Quando criar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)

        //Declaração de variáveis
        //Botões:
        val btnAvancar: Button = findViewById(R.id.btnAvancar)
        val btnVoltar: Button = findViewById(R.id.btnVoltar)

        //Intents:
        val intentMain = Intent(this, MainActivity::class.java)
        val intentCriarConta2 = Intent(this, CriarConta2::class.java)


        //Verificando se há algum estado salvo anteriormente para restaurar as informações salvadas
        if(savedInstanceState != null)
        {
            nome = savedInstanceState.getString("nome","")
            email = savedInstanceState.getString("email", "")
            telefone = savedInstanceState.getString("telefone","")
        }

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

    //Salvando pela primeira vez as informações digitadas em um Estado de UI em Bundle
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Declaração de variáveis
        //EditText
        val etNome : EditText = findViewById(R.id.etNome)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etTelefone: EditText = findViewById(R.id.etTelefone)

        //Guardando as informações digitas nas propriedades do objeto Bundle do SavedInstanceState pela primeira vez
        outState.putString("nome", etNome.text.toString())
        outState.putString("email",etEmail.text.toString())
        outState.putString("telefone",etTelefone.text.toString())
    }

    private fun cadastrarUsuário(){
        //Lógica para cadastrar o usuário no banco de dados
        //Aqui pode chamar um API ou salvar os dados em um banco de dados local
    }
}