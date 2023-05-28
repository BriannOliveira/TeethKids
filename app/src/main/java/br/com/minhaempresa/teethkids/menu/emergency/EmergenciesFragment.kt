package br.com.minhaempresa.teethkids.menu.emergency

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentEmergenciesBinding
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.DetailEmergencyFragment
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Emergency
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.EmergencyAdapter
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.emergencyList
import com.google.firebase.firestore.FirebaseFirestore


class EmergenciesFragment : Fragment(), EmergencyAdapter.RecyclerViewEvent{

    //criando a vinculação de todos os elementos do layout
    private var _binding: FragmentEmergenciesBinding? = null

    //uma maneira de garantir que o _binding não seja nulo, caso contrário ele lança uma NullException
    private val binding get() = _binding!!


    private val db = FirebaseFirestore.getInstance()


    //método para inflar o layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEmergenciesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dadosTeste()

        //configurar recyclerview
        var recyclerView = binding.rvEmergencias
        var emergencyAdapter = EmergencyAdapter(emergencyList, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true) //otimizar o recyclerview com um tamanho fixo
        recyclerView.adapter = emergencyAdapter

        val user = arguments?.getString("user")
        Log.d("UserArgument","${user}")

        //configurar nome do dentista na textview
        if (user != null){
            val docRef = db.collection("user").document(user)
            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    Log.d("DocumentSnapshot data:","${document.data}")
                    val data = document.getString("name")
                    binding.tvWelcome2.text = data
                } else {
                    Log.d("Document don't exist","No such document")
                }
            }.addOnFailureListener{ exception ->
                Log.d("Document","get failed with", exception)
            }
        } else {
            Log.d("User?","User don't exist.")
        }


        binding.sStatus.setOnCheckedChangeListener{_ , isChecked ->
            var status = true
            if (isChecked){
                if (user != null){
                    val docRef = db.collection("user").document(user)
                    docRef.update("status",status)
                } else {
                    Log.d("User?","User don't exist")
                }
            } else {
                status = false
                if(user != null){
                    val docRef = db.collection("user").document(user)
                    docRef.update("status",status)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
    //função do recycler view event
    override fun onItemClick(position: Int) {

        //elemento da lista que foi clicado guardado
        val emergency = emergencyList[position]

        //procedimento para passar o elemento da lista como argumento para o detail fragment
        val bundle = Bundle()
        bundle.putParcelable("emergency",emergency)
        val detailEmergencyFragment = DetailEmergencyFragment()
        detailEmergencyFragment.arguments = bundle

        //requireActivity para pegar o supportFragmentManager e trocar as telas dentro do layout container
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_menu,detailEmergencyFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun dadosTeste(){
        //função só para preencher a recyclerview por enquanto
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

