package br.com.minhaempresa.teethkids.menu.reputation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentReputationBinding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Emergency
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.EmergencyAdapter
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Status
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.emergencyList
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.Avaliation
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.AvaliationAdapter
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.DetailAvaliationFragment
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.avaliationslist
import br.com.minhaempresa.teethkids.signUp.model.CustomResponse
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.Locale


class ReputationFragment : Fragment(), EmergencyAdapter.RecyclerViewEvent {

    private var _binding : FragmentReputationBinding? = null
    private val binding get() = _binding!!
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private lateinit var listener: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReputationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseMyUser.getCurrentUser()

        if(currentUser != null){
            inflateAvaliations(currentUser.uid)
        }

        //configurando cardview estilizado
        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .setTopLeftCorner(CornerFamily.ROUNDED,80f)
            .setTopRightCorner(CornerFamily.ROUNDED,80f)
            .build()

        binding.cvAvaliations.shapeAppearanceModel = shapeAppearanceModel

        binding.swipeRefreshRv.setOnRefreshListener {
            if (currentUser != null){
                inflateAvaliations(currentUser.uid)
            }
            binding.swipeRefreshRv.isRefreshing = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {

        //elemento da lista que foi clicado
        val avaliation = avaliationslist[position]

        //passar a avaliação como argumento
        val bundle = Bundle()
        bundle.putParcelable("avaliation",avaliation)
        val detailAvaliationFragment = DetailAvaliationFragment()
        detailAvaliationFragment.arguments = bundle

        //chamar o fragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_menu,detailAvaliationFragment)
            .addToBackStack(null)
            .commit()

    }

    private fun findAvaliations(userId: String): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        return functions
                .getHttpsCallable("findAvaliations")
                .call(userId)
                .continueWith { task ->
                    gson.toJson(task)
                    val result =
                        gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                    result
                }
    }

    private fun inflateAvaliations(userId: String){

        findAvaliations(userId).addOnCompleteListener { task ->

            //deserializar a lista de emergências
            val jsonPayload = gson.toJson(task.result.payload)
            val listType = object: TypeToken<List<Avaliation>>() {}.type
            avaliationslist = gson.fromJson(jsonPayload, listType)
            avaliationslist.sortByDescending { it.time._seconds * 1000 + it.time._nanoseconds/1000000}

            //configurando média da progress bar
            var total = 0.0
            for (item in avaliationslist){

                total += item.rate
            }
            var media = (total/(avaliationslist.size))*20
            if (avaliationslist.isNotEmpty()){
                binding.tvImNew.visibility = View.GONE
                binding.pbAverage.progress = media.toInt()
                val numberFormat = NumberFormat.getInstance(Locale.getDefault())
                binding.tvPerCentAvarage.setText("${numberFormat.format(media)} %")
            } else {
                binding.pbAverage.progress = 0
                binding.tvPerCentAvarage.setText("--")
                binding.tvImNew.visibility = View.VISIBLE
            }

            //configurar o recyclerView
            var recyclerView = binding.rvAvaliations
            var avaliationAdapter = AvaliationAdapter(avaliationslist,this)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = avaliationAdapter
        }
    }
}