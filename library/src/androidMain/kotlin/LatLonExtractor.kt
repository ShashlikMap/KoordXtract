package com.shashlik.koordxtract.fibonacci

import android.content.Intent
import android.util.Log

object LatLonExtractor {
    suspend fun extractFromIntent(intent: Intent): LatLon {
        Log.d("koordxtract", "onNewIntent: $intent")
        return LatLon(0.0, 0.0)
    }
}