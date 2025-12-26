package com.shashlik.koordxtract

import LatLon
import LatLonExtractError
import LatLonExtractor
import android.content.Intent
import android.util.Log
import arrow.core.Either

suspend fun LatLonExtractor.extractFromIntent(intent: Intent): Either<LatLonExtractError, LatLon> {
    Log.d("koordxtract", "onNewIntent: $intent")
    return extractFromStringData(intent.getStringExtra(Intent.EXTRA_TEXT))
}