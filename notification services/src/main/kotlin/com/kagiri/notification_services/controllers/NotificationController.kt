package com.kagiri.notification_services.controllers

import com.kagiri.notification_services.models.NotificationRequest
import com.kagiri.notification_services.models.NotificationResponse
import com.kagiri.notification_services.service.NotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class NotificationController(
    private val notificationService: NotificationService
) {

    @PostMapping("/notify")
    fun handleNotificationRequest(@RequestBody notificationRequest: NotificationRequest): ResponseEntity<NotificationResponse> {
        notificationService.sendNotificationToDevice(notificationRequest = notificationRequest)

        return ResponseEntity.of(Optional.of(NotificationResponse(status = 200, message = "")))
    }
}