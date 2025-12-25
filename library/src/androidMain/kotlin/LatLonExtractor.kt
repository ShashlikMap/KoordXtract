package com.shashlik.koordxtract

import LatLon
import android.content.Intent
import android.util.Log
import extractFromStringData

object LatLonExtractor {
    suspend fun extractFromIntent(intent: Intent): LatLon? {
        Log.d("koordxtract", "onNewIntent: $intent")
        return extractFromStringData(intent.getStringExtra(Intent.EXTRA_TEXT))
    }
}