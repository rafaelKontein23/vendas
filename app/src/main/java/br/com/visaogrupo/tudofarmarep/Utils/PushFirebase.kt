package br.com.visaogrupo.tudofarmarep.Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class PushFirebase : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d("", "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {

        }

        remoteMessage.notification?.let {
            Log.d("TAG", "Message Notification Body: ${it.body}")
        }
        enviarNotificacao(remoteMessage.from, remoteMessage.notification!!.body, this)
    }

    private fun enviarNotificacao(from: String?, body: String?, context: Context) {
             MainScope().launch {
                 val intent = Intent(context, MainActivity::class.java)
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 val requestCode = 0
                 val pendingIntent = PendingIntent.getActivity(
                     context,
                     requestCode,
                     intent,
                     PendingIntent.FLAG_IMMUTABLE,
                 )

                 val channelId = "fcm_default_channel"
                 val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                 val notificationBuilder = NotificationCompat.Builder(context, channelId)
                     .setSmallIcon(R.mipmap.ic_launcher)
                     .setContentTitle("FCM Message")
                     .setContentText("messageBody")
                     .setAutoCancel(true)
                     .setSound(defaultSoundUri)
                     .setContentIntent(pendingIntent)

                 val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     val channel = NotificationChannel(
                         channelId,
                         "Channel human readable title",
                         NotificationManager.IMPORTANCE_DEFAULT,
                     )
                     notificationManager.createNotificationChannel(channel)
                 }

                 val notificationId = 0
                 notificationManager.notify(notificationId, notificationBuilder.build())
             }
    }

    fun recuperaDeviceToken():String{
        var deviceToken = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
             deviceToken = task.result
        })
        return deviceToken
    }
}