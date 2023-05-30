package br.com.minhaempresa.teethkids.helper

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class FirebaseMyUser {

    //companion object para o usar os método sem precisar instanciar as classes
    companion object{

        fun getCurrentUser(): FirebaseUser?{
            var usuario = FirebaseAuth.getInstance()
            return usuario.currentUser
        }

        fun updateUserName(name: String){
            try {
                var user = getCurrentUser()
                var profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                if(user!=null){
                    user.updateProfile(profile).addOnCompleteListener{ task ->
                        if(!task.isSuccessful){
                            Log.d("Perfil","Erro ao atualizar o nome de perfil.")
                        }
                    }
                } else {
                    Log.d("Perfil?","Perfil nulo")
                }
            }catch (e: Exception){
                Log.d("Failed","Não foi possível atualizar o usuário.")
            }
        }
    }
}
