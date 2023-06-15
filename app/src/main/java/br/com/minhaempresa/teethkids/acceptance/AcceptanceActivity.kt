package br.com.minhaempresa.teethkids.acceptance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.ActivityAcceptanceBinding

class AcceptanceActivity : AppCompatActivity() {

    private var _binding: ActivityAcceptanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var waitFragment: WaitFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAcceptanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //instânciando os fragments
        waitFragment = WaitFragment()

        //configurando o fragmentManager
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.add(R.id.container_acceptance, waitFragment)
        fragmentManager.commit()

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val title = TextView(this)
        title.text = "iTooth"
        val typeface = ResourcesCompat.getFont(this, R.font.alphakind)
        title.typeface = typeface
        title.setTextColor(ContextCompat.getColor(this,R.color.whitetheme))
        title.textSize = 30f
        binding.myToolbar.addView(title)

    }
}