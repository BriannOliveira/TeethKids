package br.com.minhaempresa.teethkids.signUp

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import br.com.minhaempresa.teethkids.R
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import br.com.minhaempresa.teethkids.databinding.FragmentPreviewCameraBinding
import java.io.File
import androidx.appcompat.app.AppCompatActivity



class CameraPreviewFragment : Fragment() {

    private var _binding: FragmentPreviewCameraBinding? = null
    private val binding get() = _binding!!

    // processamento de imagens
    //promessa de voltar um objeto do tipo ProcessCameraProvider
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    // selecionar a câmera frontal ou traseira
    private lateinit var cameraSelector: CameraSelector

    // imagem capturada.
    private var imageCapture: ImageCapture? = null

    // executor de threads separado
    private lateinit var imgCaptureExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        // chamar o metodo de iniciar a camera
        startCamera()

        binding.btnTirarfoto.setOnClickListener{
            takePhoto()
        }

        binding.ibtnClose.setOnClickListener {
            val signUpFragment = SignUpFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, signUpFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startCamera(){
        //quando a cameraProviderFuture estiver pronta, irá fazer isso (callback)
        cameraProviderFuture.addListener({

            imageCapture = ImageCapture.Builder().build()

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }

            try {
                // abrir o preview
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector, preview, imageCapture)

            } catch (e: Exception){
                Log.e("CameraPreview","Falha ao abrir a câmera")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto()
    {
        // código para tirar foto
        imageCapture?.let {

            //nome do arquivo para gravar a foto.
            val fileName = "FOTO_JPEG_${System.currentTimeMillis()}"
            val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i("CameraPreview", "A imagem foi salva no diretório: ${file.toURI()}")

                        //passar a imagem para o PhotoTakenFragment
                        val photoTakenFragment = PhotoTakenFragment()
                        val bundle = Bundle()
                        bundle.putString("photoPath", file.absolutePath)
                        photoTakenFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main, photoTakenFragment)
                            .addToBackStack(null)
                            .commit()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(binding.root.context, "Erro ao salvar foto", Toast.LENGTH_LONG).show()
                        Log.e("CameraPreview", "Exceção ao gravar arquivo da foto: $exception")
                    }
                }
            )
        }
    }
}