package br.com.minhaempresa.teethkids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent

class CriarConta2 : AppCompatActivity() {

    private var curriculo = ""
    lateinit var etCurriculo: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta2)

        //Declaração de variáveis
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)
        // btnAvancar = findViewById<Button>(R.id.btnAvancar)
        val intentCriarConta = Intent(this, CriarConta::class.java)

        //Verifica se há um estado salvo
        if(savedInstanceState!=null)
        {
            curriculo = savedInstanceState.getString("curriculo","")
        }

        //Insere os dados salvos anteriormente
        etCurriculo = findViewById(R.id.etCurriculo)
        etCurriculo.setText(curriculo)

        //Ainda tem que configurar o botão para avançar e criar uma nova intent

        //Voltar para a parte inicial do cadastro
        btnVoltar.setOnClickListener()
        {
            curriculo = etCurriculo.text.toString()
            startActivity(intentCriarConta)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Para salvar os valores dos campos EditText
        outState.putString("nome",etCurriculo.text.toString())
    }
}