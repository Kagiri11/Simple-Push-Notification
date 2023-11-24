package com.kagiri.notification_services.service

import com.google.api.client.util.Value
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.lang.System.Logger
import javax.annotation.PostConstruct

@Service
class NotificationService {

    @Value("\${app.firebase-configuration-file}")
    private val firebaseConfigPath: String = ""

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    fun initialize() {
        try {
            val credentials = GoogleCredentials.fromStream(ClassPathResource(firebaseConfigPath).inputStream)
            val options = FirebaseOptions.Builder().setCredentials(credentials).build()
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                logger.info("Firebase application initialized")
            }

        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    fun sendNotification() {

    }

}