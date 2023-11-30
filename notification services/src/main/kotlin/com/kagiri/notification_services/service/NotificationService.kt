package com.kagiri.notification_services.service

import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.google.gson.GsonBuilder
import com.kagiri.notification_services.models.NotificationRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class NotificationService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun sendNotificationToDevice(notificationRequest: NotificationRequest) {
        val message = getPreconfiguredMessageToToken(notificationRequest = notificationRequest)
        val gson = GsonBuilder().setLenient().setPrettyPrinting().create()
        val jsonString = gson.toJson(message)
        val response = sendMessage(message = message)
        logger.info("Sent message $jsonString to token: ${notificationRequest.token}, response was: $response")
    }

    private fun sendMessage(message: Message): String? {
        return FirebaseMessaging.getInstance().sendAsync(message).get()
    }

    private fun getPreconfiguredMessageToToken(notificationRequest: NotificationRequest): Message {
        return messageBuilder(notificationRequest)
            .setToken(notificationRequest.token)
            .build()
    }

    private fun provideAndroidConfig(topic: String) = AndroidConfig.builder()
        .setTtl(Duration.ofMinutes(2).toMillis())
        .setCollapseKey(topic)
        .setPriority(AndroidConfig.Priority.HIGH)
        .setNotification(AndroidNotification.builder().setTag(topic).build())
        .build()

    private fun provideAPNSConfig(topic: String) = ApnsConfig.builder()
        .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build())
        .build()

    fun messageBuilder(request: NotificationRequest): Message.Builder {
        val androidConfig = provideAndroidConfig(request.topic)
        val apnsConfig = provideAPNSConfig(request.topic)
        val notification = Notification.builder()
            .setTitle(request.title)
            .setBody(request.body)
            .build()

        return Message.builder()
            .setApnsConfig(apnsConfig)
            .setAndroidConfig(androidConfig)
            .setNotification(notification)
    }
}