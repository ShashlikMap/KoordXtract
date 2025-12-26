package com.shashlik.koordxtract

import LatLon
import LatLonExtractError
import android.content.Intent
import android.util.Log
import arrow.core.Either
import extractFromStringData

object LatLonExtractor {
    suspend fun extractFromIntent(intent: Intent): Either<LatLonExtractError, LatLon> {
        Log.d("koordxtract", "onNewIntent: $intent")
        return extractFromStringData(intent.getStringExtra(Intent.EXTRA_TEXT))
    }
}