package com.okariastudio.triger

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.okariastudio.triger.data.database.DatabaseProvider
import com.okariastudio.triger.data.firebase.FirebaseService
import com.okariastudio.triger.data.repository.GerRepository
import com.okariastudio.triger.ui.theme.TriGerTheme
import com.okariastudio.triger.viewmodel.GerViewModel
import com.okariastudio.triger.viewmodel.QuizViewModel
import com.okariastudio.triger.viewmodel.SettingsViewModel
import com.okariastudio.triger.worker.NotificationWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val preferences by lazy {
        getSharedPreferences("Geriou", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_notification_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(getDelay(), TimeUnit.MILLISECONDS)
                .build()
        )

        val gerRepository = GerRepository(
            gerDao = DatabaseProvider.getDatabase(this).gerDao(),
            firebaseService = FirebaseService()
        )
        val quizViewModel =
            QuizViewModel(gerRepository)

        val gerViewModel =
            GerViewModel(gerRepository, preferences)

        val settingsViewModel =
            SettingsViewModel(gerRepository, preferences)

        val launch = preferences.getInt("launch_count", 0) + 1
        preferences.edit().putInt("launch_count", launch).apply()

        lifecycleScope.launch {
            gerViewModel.synchronizeData()
            delay(2000)
            gerViewModel.fetchGersForToday()
            delay(1000)
            keepSplashScreen = false
        }

        enableEdgeToEdge()

        if (launch % 5 == 0) {
            val manager = ReviewManagerFactory.create(this)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println(task.result)
                } else {
                    @ReviewErrorCode val reviewErrorCode =
                        (task.getException() as ReviewException).errorCode
                    println(reviewErrorCode)
                }
            }
        }

        setContent {
            TriGerTheme(settingsViewModel = settingsViewModel) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        quizViewModel = quizViewModel,
                        settingsViewModel = settingsViewModel,
                        gerViewModel = gerViewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        preferences.edit().putString("lastDayOpened", LocalDate.now().toString()).apply()
    }

    private fun getDelay(): Long {
        val now = LocalTime.now().toNanoOfDay() / 1_000_000
        val notificationTime = LocalTime.of(18, 30).toNanoOfDay() / 1_000_000

        return if (notificationTime > now) {
            notificationTime - now
        } else {
            24 * 60 * 60 * 1000 - (now - notificationTime)
        }
    }
}