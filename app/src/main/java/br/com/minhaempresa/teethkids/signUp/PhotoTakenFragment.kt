package br.com.minhaempresa.teethkids.signUp

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentPhotoTakenBinding
import br.com.minhaempresa.teethkids.helper.FirebaseMyUser
import br.com.minhaempresa.teethkids.menu.profile.ProfileFragment
import br.com.minhaempresa.teethkids.signUp.CameraPreviewFragment.Companion.SOURCE_PROFILE
import br.com.minhaempresa.teethkids.signUp.CameraPreviewFragment.Companion.SOURCE_SIGNUP
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class PhotoTakenFragment : Fragment() {

    private var _binding: FragmentPhotoTakenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoTakenBinding.inflate(inflater, container, false)

        //recuperando a foto salva
        val filePath = arguments?.getString("photoPath")
        if (filePath != null){
            val file = File(filePath)
            val photoUri = FileProvider.getUriForFile(requireContext(),"br.com.minhaempresa.teethkids.fileprovider",file)

            //carregar a imagem no imgView
            Glide.with(requireContext()).load(photoUri).into(binding.ivPhotoTaken)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawable = GradientDrawable()
        drawable.setColor(Color.WHITE)
        drawable.cornerRadii = floatArrayOf(0f, 0f, 20f, 20f, 20f, 20f, 0f, 0f)

        binding.ivPhotoTaken.background = drawable

        binding.btnConfirmPhoto.setOnClickListener {
            //salvar a foto no storage
            val currentUser = FirebaseMyUser.getCurrentUser()
            if(currentUser != null){
                val storageRef = Firebase.storage.reference.child("dentista/${currentUser.uid}")
                val filePath = arguments?.getString("photoPath")
                if (filePath != null) {
                    val file = File(filePath)
                    val fileUri = Uri.fromFile(file)
                    val uploadTask = storageRef.putFile(fileUri)

                    uploadTask.addOnProgressListener { taskSnapshot ->
                        val progress =
                            (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                        System.out.println("Upload is $progress% done")
                    }.addOnPausedListener {
                        System.out.println("Upload is paused")
                    }.addOnFailureListener {
                        // Trata o caso em que o upload falha
                    }.addOnSuccessListener {
                        // Trata o caso em que o upload foi bem sucedido
                        //passar a imagem para o SignUp
                        val source = arguments?.getString("source")
                        if(source == SOURCE_PROFILE){
                            val profileFragment = ProfileFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container_menu,profileFragment)
                                .commit()
                        } else if (source == SOURCE_SIGNUP){
                            val signUpFragment = SignUpFragment()
                            val bundle = Bundle()
                            bundle.putString("photoPath", file.absolutePath)
                            signUpFragment.arguments = bundle

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main, signUpFragment)
                                .commit()
                        }
                    }
                }
            }
        }

        binding.btnVoltar.setOnClickListener{
            val cameraPreviewFragment = CameraPreviewFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, cameraPreviewFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val ARG_FILE_PATH = "filePath"

        fun newInstance(file: File):PhotoTakenFragment {
            val fragment = PhotoTakenFragment()
            val args = Bundle()
            args.putString(ARG_FILE_PATH, file.absolutePath)

            fragment.arguments = args
            return fragment
        }
    }
}