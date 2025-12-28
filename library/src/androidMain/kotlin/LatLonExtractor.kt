package com.shashlik.koordxtract

import LatLon
import LatLonExtractError
import LatLonExtractor
import android.content.Intent
import arrow.core.Either

/**
 * Convenient extension to extract [LatLon] from Android [Intent]
 */
@Suppress("unused")
suspend fun LatLonExtractor.extractFromIntent(intent: Intent): Either<LatLonExtractError, LatLon> =
    extractFromStringData(intent.getStringExtra(Intent.EXTRA_TEXT))
