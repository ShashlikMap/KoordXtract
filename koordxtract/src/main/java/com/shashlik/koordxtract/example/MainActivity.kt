package com.shashlik.koordxtract.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.shashlik.koordxtract.example.ui.theme.KoordXtractTheme
import LatLon
import LatLonExtractError
import LatLonExtractor
import android.util.Log
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.shashlik.koordxtract.extractFromIntent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

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

    private sealed interface State {
        data object Loading : State
        data class Result(val result: Either<LatLonExtractError, LatLon>) : State
    }

    private val xTractor = LatLonExtractor()
    private var latestLatLon by mutableStateOf<Option<State>>(
        None
    )

    private var linkTestJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            handlingIntent(intent)
        }
        enableEdgeToEdge()
        setContent {
            KoordXtractTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        LatLonResult()
                    }
                }
            }
        }
    }

    @Composable
    private fun BoxScope.LatLonResult() {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                linksTest()
            }) {
                Text("Start Links Test")
            }

            Spacer(modifier = Modifier.height(48.dp))

            latestLatLon.fold({
                Text(text = "Or share location manually")
            }, { result ->
                when (result) {
                    State.Loading -> {
                        CircularProgressIndicator()
                    }

                    is State.Result -> {
                        result.result.fold({
                            Text(text = "Error: $it")
                        }, {
                            Text(text = "LatLon:")
                            Text(text = it.toString(), fontSize = 15.sp)
                        })
                    }
                }

            })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handlingIntent(intent)
        }
    }

    private fun linksTest() {
        linkTestJob?.cancel()
        linkTestJob = lifecycleScope.launch {
            val passed = RandomLinksAcrossTheWorld.count { link ->
                val result = xTractor.extractFromStringData(link)
                Log.d(TAG, "link: $link, result: $result")
                result.isRight()
            }
            Log.d(
                TAG,
                "Links test passed $passed out of ${RandomLinksAcrossTheWorld.size}"
            )
        }
    }

    private fun handlingIntent(intent: Intent) {
        latestLatLon = Some(State.Loading)
        lifecycleScope.launch {
            latestLatLon = Some(State.Result(xTractor.extractFromIntent(intent)))
        }
    }
}