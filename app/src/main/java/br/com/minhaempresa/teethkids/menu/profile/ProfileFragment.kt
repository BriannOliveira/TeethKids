package br.com.minhaempresa.teethkids.menu.profile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    ): View {
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


        binding.btnEditData.setOnClickListener {

            //ocultando os textviews e mostrando as edittexts
            binding.tvEditName.visibility = View.INVISIBLE
            binding.tvEditPhone.visibility = View.INVISIBLE
            binding.tvEditResume.visibility = View.INVISIBLE
            binding.v1.visibility = View.INVISIBLE
            binding.v2.visibility = View.INVISIBLE
            binding.etEditName.visibility = View.VISIBLE
            binding.etEditPhone.visibility = View.VISIBLE
            binding.etEditResume.visibility = View.VISIBLE
            binding.btnSaveData.visibility = View.VISIBLE
            binding.btnEditData.visibility = View.INVISIBLE

            //transferindo os textos para as editexts
            binding.etEditName.setText(binding.tvEditName.text)
            binding.etEditPhone.setText(binding.tvEditPhone.text)
            binding.etEditResume.setText(binding.tvEditResume.text)

            binding.etEditName.requestFocus()
            binding.etEditPhone.requestFocus()
            binding.etEditResume.requestFocus()
        }

        if(user != null){
            binding.btnSaveData.setOnClickListener {

                var docRefUser = db.collection("user").document(user.uid)

                //iniciar um lote de gravação para gravar múltiplos campos no mesmo documento
                val batch = db.batch()

                //salvando os dados atualizados do user no firestore
                batch.update(docRefUser,"name",binding.etEditName.text.toString())
                batch.update(docRefUser,"phone",binding.etEditPhone.text.toString())
                batch.update(docRefUser,"resume",binding.etEditResume.text.toString())

                //compromete o lote
                batch.commit()
                    .addOnSuccessListener {

                        //ocultando os edittexts e mostrando as textviews
                        binding.tvEditName.visibility = View.VISIBLE
                        binding.tvEditPhone.visibility = View.VISIBLE
                        binding.tvEditResume.visibility = View.VISIBLE
                        binding.v1.visibility = View.VISIBLE
                        binding.v2.visibility = View.VISIBLE
                        binding.etEditName.visibility = View.GONE
                        binding.etEditPhone.visibility = View.GONE
                        binding.etEditResume.visibility = View.GONE
                        binding.btnSaveData.visibility = View.GONE
                        binding.btnEditData.visibility = View.VISIBLE

                        //carregando as textviews com os novos dados
                        docRefUser.get()
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
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(),"Não foi possível salvar os dados",Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}