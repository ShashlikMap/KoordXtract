package com.shashlikmap.koordxtractdemo

import LatLon
import LatLonExtractError
import LatLonExtractor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppLogic {
    companion object {
        private val TAG = "KoordXTract"

        private val RandomLinksAcrossTheWorld = listOf(
            "https://maps.app.goo.gl/wPn6i3inrCDNFD7r8",
            "https://maps.app.goo.gl/SrjpRwuVfhqyKc5HA",
            "https://maps.app.goo.gl/HSUQp6K9L8z5efAY7",
            "https://maps.app.goo.gl/nWx4HL35gqPhHrif8",
            "https://maps.app.goo.gl/tkQ8rmvUKTxnstZM8",
            "https://maps.app.goo.gl/VWAUD4NbM45PDze36",
            "https://maps.app.goo.gl/7C7nDGQ15kKD3vCv5",
            "https://maps.app.goo.gl/r6AfdfewurGHRiLe7"
        )
    }

    //Apple
    //https://maps.apple/p/ogrw-5mF-Pjyax

    sealed interface State {
        data object Loading : State
        data class Result(val result: Either<LatLonExtractError, LatLon>) : State
    }

    private val xTractor = LatLonExtractor()
    var latestLatLon by mutableStateOf<Option<State>>(
        None
    )
        private set

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var linkTestJob: Job? = null

    fun linksTest() {
        linkTestJob?.cancel()
        linkTestJob = scope.launch {
            val passed = RandomLinksAcrossTheWorld.count { link ->
                val result = xTractor.extractFromStringData(link)
                Logger.d { "link: $link, result: $result" }
                result.isRight()
            }
            Logger.d { "Links test passed $passed out of ${RandomLinksAcrossTheWorld.size}" }
        }
    }

    fun handlingData(data: String) {
        latestLatLon = Some(State.Loading)
        scope.launch {
            latestLatLon = Some(State.Result(xTractor.extractFromStringData(data)))
        }
    }
}