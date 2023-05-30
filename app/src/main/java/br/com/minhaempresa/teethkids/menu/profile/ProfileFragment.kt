package br.com.minhaempresa.teethkids.menu.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentProfileBinding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val db =  FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //configurando cardview estilizado
        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .setTopLeftCorner(CornerFamily.ROUNDED,100f)
            .setTopRightCorner(CornerFamily.ROUNDED,100f)
            .build()
        binding.cvEditProfile.shapeAppearanceModel = shapeAppearanceModel

        //carregando as informações do usuário nas textviews
        var user = FirebaseMyUser.getCurrentUser()
        if (user!=null)
        {
            var docRef = db.collection("user").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if(document != null)
                    {
                        binding.tvEditName.text = document.getString("name")
                        binding.tvEditPhone.text = document.getString("phone")
                        binding.tvEditResume.text = document.getString("resume")
                    } else {
                        Log.d("DocEdit?","Nenhum documento encontrado.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FailedEdit","Falha ao buscar documento.", exception)
                }
        } else {
            Log.d("UserEdit?","Usuário não encontrado.")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}