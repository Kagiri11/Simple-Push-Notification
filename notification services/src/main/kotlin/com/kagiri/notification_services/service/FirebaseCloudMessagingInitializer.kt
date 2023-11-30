package com.kagiri.notification_services.service

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import javax.annotation.PostConstruct

@Service
class FirebaseCloudMessagingInitializer {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    fun initialize() {
        try {
            val credentials = GoogleCredentials.fromStream(FileInputStream(File("C:\\Users\\Charles Maina\\IdeaProjects\\SimplePushNotification\\notification services\\src\\main\\resources\\firebase-admin-sdk.json")))
            val options = FirebaseOptions.Builder().setCredentials(credentials).build()
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                logger.info("Firebase application initialized")
            }

        } catch (e: Exception) {
            logger.error(e.message)
        }
    }
}