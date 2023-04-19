package br.com.minhaempresa.teethkids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore.Audio.Media
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import br.com.minhaempresa.teethkids.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    //Na inicialização da tela
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Na tela criada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Configuração para a imagem ter função para tirar selfie
        binding.imgUsuario.setOnClickListener(){

            //Condição para ver se o usuário permitiu o acesso do aplicativo à camêra,
            //Caso checkPermission não tem um valor ou for falso, ele pede a permissão
            if(!checkPermission() || checkPermission()==false){
                requestPermission()
            }

            //Caso true, ele aciona a função para tirar foto com um argumento que informa aonde a foto deve ser posta
            if (!checkPermission()==true){
                takePhoto(binding.imgUsuario)
            }
        }

        //Botão para ir para o terceiro fragmento do login
        binding.btnAvancar.setOnClickListener(){

            //Declaração de variáveis e transformando o conteúdo das variáveis em String
            val senha = binding.etSenha.text.toString()
            val email = binding.etEmail.text.toString()

            //Ação caso o campo do email e senha estiver vazio
            if(email.isEmpty()||(senha.isEmpty())){
                val snackbar = Snackbar.make(view,"Preencha o campo email", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            }
            else{
                auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener{cadastro ->
                    if (cadastro.isSuccessful) {
                        findNavController().navigate(R.id.action_LoginFragment_to_LoginFragment2)
                    }
                }.addOnFailureListener{

                }
            }
        }

        //Botão voltar para a MainActivity
        binding.btnVoltar.setOnClickListener()
        {
            findNavController().navigate(R.id.action_LoginFragment_comeBack)
        }

    }

    //Função quando o usuário inicia uma ação de permissão entre o SO e o aplicativo
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CAMERA_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permissão concedida
            }else {
                Toast.makeText(requireContext(),"Permissão de camêra negada", Toast.LENGTH_SHORT).show()
                    //Permissão negada
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            binding.imgUsuario.setImageBitmap(bitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Função que checa se o usuário permitiu o acesso à câmera e retorna um Boolean
    private fun checkPermission(): Boolean{
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    //Função que pede permissão do acesso à câmera para o aplicativo
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION_CODE
        )
    }

    //Função para tirar foto e substituir a foto tirada por alguma ImageView
    private fun takePhoto(view: View){
        //Cria uma nova intenção do tipo "Media.Store.ACTION_IMAGE_CAPTURE" para iniciar a camêra do dispositivo
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhoto ->
            takePhoto.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile : File? = try {
                    createImageFile()               //Cria um novo arquivo para armazenar a imagem capturada pela câmera.
                } catch (ex: IOException){
                    null                            //Erro ao criar o arquivo
                }

                photoFile?.also {
                    imageUri = FileProvider.getUriForFile(requireContext(), "com.example.android.fileprovider", it)
                    takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    //Função para criar um arquivo em que a imagem capturada possa ser armazenada
    @Throws(IOException::class)
    private fun createImageFile(): File{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir                  //Esse arquivo será salvo no diretório específico do aplicativo para armazenamento de imagens.
        ).apply {
            val currentPhotoPath = absolutePath
        }
    }

    //Companion object permite que você defina propriedades e métodos em uma classe
    companion object{
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION_CODE = 100
        private lateinit var imageUri : Uri
    }
}
