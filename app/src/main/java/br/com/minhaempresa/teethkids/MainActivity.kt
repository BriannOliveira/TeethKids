package br.com.minhaempresa.teethkids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCriarConta: Button = findViewById(R.id.btnCriarconta)
        val intentCriarConta = Intent(this, CriarConta::class.java)

        btnCriarConta.setOnClickListener()
        {
            startActivity(intentCriarConta)
            finish()
        }
    }
}