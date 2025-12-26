package com.shashlik.koordxtract

import LatLon
import LatLonExtractError
import LatLonExtractor
import android.content.Intent
import arrow.core.Either

suspend fun LatLonExtractor.extractFromIntent(intent: Intent): Either<LatLonExtractError, LatLon> {
    return extractFromStringData(intent.getStringExtra(Intent.EXTRA_TEXT))
}