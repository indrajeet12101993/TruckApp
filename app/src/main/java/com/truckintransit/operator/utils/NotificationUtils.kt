package com.truckintransit.operator.utils

import android.graphics.BitmapFactory
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.truckintransit.operator.R



class NotificationUtils(var context: Context) {


    private  var notificationManagerCompat: NotificationManagerCompat
    private  var notificationManager: NotificationManager

    init {
        notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }



    fun registerNotificationChannelChannel(channelId: String, channelName: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = channelDescription
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun normalNotification(notificationId: Int,channelId: String,title: String,description:String){


        val notifyBuilder = NotificationCompat.Builder(context,channelId )
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_trucking)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        notificationManagerCompat.notify(notificationId, notifyBuilder.build())

    }

    fun triggerNotification(
        targetNotificationActivity: Class<*>,
        channelId: String,
        title: String,
        text: String,
        bigText: String,
        priority: Int,
        autoCancel: Boolean,
        notificationId: Int
    ) {

        val intent = Intent(context, targetNotificationActivity)
        intent.putExtra("count", title)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_trucking)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trucking))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setChannelId(channelId)
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(notificationId, builder.build())
    }

    fun triggerNotification(targetNotificationActivity: Class<*>, channelId: String, title: String, text: String,
                            bigText: String, priority: Int, autoCancel: Boolean, notificationId: Int, pendingIntentFlag: Int) {

        val intent = Intent(context, targetNotificationActivity)
        intent.putExtra("count", title)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, pendingIntentFlag)


        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_trucking)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trucking))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setChannelId(channelId)
            .setAutoCancel(true)

        notificationManagerCompat.notify(notificationId, builder.build())
    }

    fun triggerNotificationWithBackStack(
        targetNotificationActivity: Class<*>,
        channelId: String,
        title: String,
        text: String,
        bigText: String,
        priority: Int,
        autoCancel: Boolean,
        notificationId: Int,
        pendingIntentFlag: Int
    ) {

        val intent = Intent(context, targetNotificationActivity)
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addNextIntentWithParentStack(intent)
        intent.putExtra("count", title)
        val pendingIntent = taskStackBuilder.getPendingIntent(0, pendingIntentFlag)


        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_trucking)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trucking))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setChannelId(channelId)
            .setAutoCancel(true)

        notificationManagerCompat.notify(notificationId, builder.build())
    }

    fun updateWithPicture(
        targetNotificationActivity: Class<*>,
        title: String,
        text: String,
        channelId: String,
        notificationId: Int,
        bigpictureString: String,
        pendingIntentflag: Int
    ) {

        val intent = Intent(context, targetNotificationActivity)
        intent.putExtra("count", title)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, pendingIntentflag)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_trucking)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trucking))
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setChannelId(channelId)
            .setAutoCancel(true)

        val androidImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_trucking)
        builder.setStyle(
            NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle(
                bigpictureString
            )
        )
        notificationManager.notify(notificationId, builder.build())
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }


}