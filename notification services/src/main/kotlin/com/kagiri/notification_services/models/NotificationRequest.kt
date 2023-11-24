package com.kagiri.notification_services.models

data class NotificationRequest(
    val token: String,
    val body: String,
    val topic: String,
    val title: String
)
