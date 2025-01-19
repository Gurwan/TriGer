package com.okariastudio.triger.data.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class Tracking(private val context: Context) {
    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this.context)

    fun logGerLearned(ger: String) {
        try {
            analytics.logEvent("ger_learned") {
                param("deskin", 1)
                param("ger", ger)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logFilterApply(min: Int, max: Int) {
        try {
            analytics.logEvent("filter_applied") {
                param("filter", 1)
                param("filter_min", min.toLong())
                param("filter_max", max.toLong())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}