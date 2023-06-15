package br.com.minhaempresa.teethkids.acceptance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentWaitBinding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class WaitFragment : Fragment() {

    private var _binding: FragmentWaitBinding? = null
    private val binding get() = _binding!!
    private lateinit var listener: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWaitBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseMyUser.getCurrentUser()

        binding.pbWait.visibility = View.VISIBLE
        listener = FirebaseFirestore.getInstance().collection("acceptances").addSnapshotListener { snapshot, e ->
            if(e != null){
                Toast.makeText(requireContext(),"Houve algum erro. Por favor tente novamente!",Toast.LENGTH_LONG).show()
                //mudar para fragment anterior
                return@addSnapshotListener
            }

            if(snapshot != null && !snapshot.isEmpty){
                for(docChange in snapshot.documentChanges){
                    if(docChange.type == DocumentChange.Type.ADDED && currentUser != null){
                        if(docChange.document.id == currentUser.uid){
                            listener.remove()
                            binding.pbWait.visibility = View.GONE
                            val mapFragment = MapFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container_acceptance,mapFragment)
                                .commit()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}