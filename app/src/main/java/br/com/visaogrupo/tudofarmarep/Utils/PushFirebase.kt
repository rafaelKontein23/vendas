package br.com.visaogrupo.tudofarmarep.Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
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
        enviarNotificacao(remoteMessage.from, remoteMessage.notification, this)
    }

    private fun enviarNotificacao(from: String?, body: RemoteMessage.Notification?, context: Context) {
             MainScope().launch {
                 if (body != null){
                     val intent = Intent(context, MainActivity::class.java)
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                     val requestCode = 0
                     val pendingIntent = PendingIntent.getActivity(
                         context,
                         requestCode,
                         intent,
                         PendingIntent.FLAG_IMMUTABLE,
                     )

                     val channelId ="fcm_default_channel"
                     val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                     val notificationBuilder: NotificationCompat.Builder =
                         NotificationCompat.Builder(context, channelId)
                             .setSmallIcon(R.drawable.logo)
                             .setColor(Color.parseColor("#ffffff"))
                             .setContentText(body.body)
                             .setLargeIcon(
                                 BitmapFactory.decodeResource(
                                     applicationContext.resources,
                                     R.drawable.icone_push
                                 )
                             )
                             .setContentTitle(body.title)
                             .setStyle(
                                 NotificationCompat.BigTextStyle()
                                     .bigText(body.body)
                             )
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

                     val notificationId = System.currentTimeMillis().toInt()
                     notificationManager.notify(notificationId, notificationBuilder.build())
                 }

             }
    }

    fun recuperaDeviceToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            SistemaUtils.deviceToken =  task.result
        })
    }
}