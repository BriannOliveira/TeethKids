package br.com.minhaempresa.teethkids.menu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentHomeBinding
import br.com.minhaempresa.teethkids.login.MainActivity
import br.com.minhaempresa.teethkids.menu.MenuActivity
import br.com.minhaempresa.teethkids.menu.recyclerViewHome.Emergency
import br.com.minhaempresa.teethkids.menu.recyclerViewHome.EmergencyAdapter
import br.com.minhaempresa.teethkids.menu.recyclerViewHome.emergencyList
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), EmergencyAdapter.RecyclerViewEvent{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var status: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //função para buscar dados no Firestore para a recyclerView
        buscarDados()

        var recyclerView: RecyclerView = binding.rvEmergencias

        //Configurando o RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = EmergencyAdapter(emergencyList, this, this.activity as MenuActivity)



        return binding.root
    }

    override fun onItemClick(position: Int) {
        val emergency = emergencyList[position]

        //passar para o próximo fragment

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.sStatus.setOnCheckedChangeListener{buttonView, isChecked ->
            val newField : MutableMap<String, Any> = HashMap<String, Any>().apply {
                put("status", status!!)
            }
            val db = (activity as MainActivity).db
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            if(isChecked){
                status = true
                //implementar o requisito de permissão de notificação
                db.collection("user")
                    .document(userId)
                    .update(newField)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(),"Status On!",Toast.LENGTH_SHORT)
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireContext(),"Não foi possível ativar seu status.", Toast.LENGTH_SHORT)
                    }
            } else {
                status = false
                //desligar as notificações
                db.collection("user")
                    .document(userId)
                    .update(newField)
                    .addOnSuccessListener{
                        Toast.makeText(requireContext(), "Status Off!",Toast.LENGTH_SHORT)
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireContext(), "Status On!", Toast.LENGTH_SHORT)
                    }
            }
        }

            //para colocar o status como disponível, precisarei criar outro banco de dados?
            //ou colocar esse dado dentro do banco de dados que já existe?


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buscarDados(){

        //função para buscar dados no banco de dados

        val emerg1 = Emergency(
            R.drawable.avatar,
            "Briann Oliveira",
            "(19)99406-1793",
            5
        )
        emergencyList.add(emerg1)

        val emerg2 = Emergency(
            R.drawable.avatar,
            "Fernando Gomes",
            "(19)99784-9876",
            4
        )
        emergencyList.add(emerg2)

        val emerg3 = Emergency(
            R.drawable.avatar,
        "Gabriela Souza",
        "(19)99387-3987",
        3
        )
        emergencyList.add(emerg3)

        val emerg4 = Emergency(
            R.drawable.avatar,
            "Joana Luiza",
            "(19)99351-1078",
            1
        )
        emergencyList.add(emerg4)
    }


}

