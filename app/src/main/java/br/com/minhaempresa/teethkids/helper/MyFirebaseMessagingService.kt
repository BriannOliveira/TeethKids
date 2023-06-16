package br.com.minhaempresa.teethkids.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.minhaempresa.teethkids.R
import br.com.minhaempresa.teethkids.login.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "br.com.minhaempresa.teethkids.helper"

class MyFirebaseMessagingService: FirebaseMessagingService() {

    val currentUser = FirebaseMyUser.getCurrentUser()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (currentUser != null){
            FirebaseFirestore.getInstance().collection("user").document(currentUser.uid).get()
                .addOnCompleteListener{doc ->
                    if(doc.isSuccessful){
                        if(doc.result["status"] == true){
                            if (message.data.isNotEmpty()){
                                triggerNotification(message.data["title"]!!, message.data["body"]!!)
                            }
                      }
                    } else {
                        Log.d("TriggerNotification", "Failed to get document")
                    }
                }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if(currentUser != null){
            FirebaseFirestore.getInstance().collection("user").document(currentUser.uid)
                .update("fcmToken",token)
        }
    }

    private fun triggerNotification(title: String, msg: String){

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.itooth_logo)
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setOnlyAlertOnce(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        //envia a notificação
        notificationManager.notify(0, builder.build())
    }
}