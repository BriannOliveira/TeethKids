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
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.DetailEmergencyFragment
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Emergency
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.EmergencyAdapter
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Status
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.emergencyList
import br.com.minhaempresa.teethkids.signUp.model.CustomResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class EmergenciesFragment : Fragment(), EmergencyAdapter.RecyclerViewEvent{

    //criando a vinculação de todos os elementos do layout
    private var _binding: FragmentEmergenciesBinding? = null

    //uma maneira de garantir que o _binding não seja nulo, caso contrário ele lança uma NullException
    private val binding get() = _binding!!
    private lateinit var functions:FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var listener: ListenerRegistration

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

        inflateEmergencies()

        //pegando o uid do user
        val user = FirebaseMyUser.getCurrentUser()

        //configurar nome do dentista na textview
        if (user != null){
            val docRef = db.collection("user").document(user.uid)
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

        //mudar o status
        binding.sStatus.setOnCheckedChangeListener{_ , isChecked ->
            var status = true
            if (isChecked){
                if (user != null){
                    val docRef = db.collection("user").document(user.uid)
                    docRef.update("status",status)
                } else {
                    Log.d("User?","User don't exist")
                }
            } else {
                status = false
                if(user != null){
                    val docRef = db.collection("user").document(user.uid)
                    docRef.update("status",status)
                }
            }
        }

        //atualizar o recyclerView
        binding.swipeRefreshRv.setOnRefreshListener {
            inflateEmergencies()
            binding.swipeRefreshRv.isRefreshing = false
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

    private fun findEmergencies(): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        return functions
            .getHttpsCallable("findEmergencies")
            .call()
            .continueWith{ task ->
                gson.toJson(task)
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }

    private fun inflateEmergencies(){

        findEmergencies().addOnCompleteListener{ task ->

            //desserializando a lista de emergências
            val jsonPayload = gson.toJson(task.result.payload)
            val listType = object: TypeToken<List<Emergency>>() {}.type
            emergencyList = gson.fromJson(jsonPayload, listType)
            emergencyList.sortBy { it.time._seconds * 1000 + it.time._nanoseconds/1000000} //filtrando por emergênia mais antiga

            if (emergencyList.isEmpty()){
                binding.tvNoEmergencies.visibility = View.VISIBLE
            } else {
                binding.tvNoEmergencies.visibility = View.GONE
            }

            //configurar recyclerview
            val recyclerView = binding.rvEmergencias
            val emergencyAdapter = EmergencyAdapter(emergencyList, this)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true) //otimizar o recyclerview com um tamanho fixo
            recyclerView.adapter = emergencyAdapter



            //ouvindo para novas emergências
            listener = FirebaseFirestore.getInstance().collection("emergencies").addSnapshotListener{snapshot, e ->
                if (e != null){
                    Log.d("ErrorListenerFirestore", "Erro em ouvir mudanças no Firestore")
                    return@addSnapshotListener
                }

                if(snapshot != null && !snapshot.isEmpty){
                    for (docChange in snapshot.documentChanges){
                        if(docChange.type == DocumentChange.Type.ADDED){
                            if (docChange.document["status"] == Status.new)
                            {
                                val newEmergency = gson.fromJson(gson.toJson(docChange.document.data),Emergency::class.java)
                                emergencyList.add(newEmergency)
                                emergencyList.sortBy { it.time._seconds * 1000 + it.time._nanoseconds/1000000 }
                                emergencyAdapter.notifyItemInserted(emergencyList.size - 1)
                            }
                        }
                    }
                }
            }
        }
    }
}

