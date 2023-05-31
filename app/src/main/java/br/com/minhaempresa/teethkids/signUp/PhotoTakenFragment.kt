package br.com.minhaempresa.teethkids.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.databinding.FragmentPhotoTakenBinding
import com.bumptech.glide.Glide
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
        val filePath = arguments?.getString(ARG_FILE_PATH)
        if (filePath != null){
            val file = File(filePath)

            //carregar a imagem no imgView
            Glide.with(requireContext()).load(file).into(binding.ivPhotoTaken)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmPhoto.setOnClickListener {
            //salvar a foto no storage
        }

        binding.ibtnClose.setOnClickListener{
            findNavController().navigate(R.id.action_PhotoTakenFragment_to_CameraPreview)
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