package com.okariastudio.triger.data.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class Tracking(private val context: Context) {
    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logGerLearned(ger: String) {
        analytics.logEvent("ger_learned") {
            param("ger", ger)
        }
    }
}