package com.shashlik.koordxtract.example

import android.app.ComponentCaller
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.shashlik.koordxtract.example.ui.theme.KoordXtractTheme
import LatLon
import LatLonExtractError
import LatLonExtractor
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.shashlik.koordxtract.extractFromIntent
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val xtractor = LatLonExtractor()
    private var latestLatLon by mutableStateOf<Option<Either<LatLonExtractError, LatLon>>>(
        None
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlingIntent(intent)
        enableEdgeToEdge()
        setContent {
            KoordXtractTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            latestLatLon.fold({
                                Text(text = "Share location")
                            }, { result ->
                                result.fold({
                                    Text(text = "Error: $it")
                                }, {
                                    Text(text = "LatLon: $it")
                                })
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        handlingIntent(intent)
    }

    fun handlingIntent(intent: Intent) {
        lifecycleScope.launch {
            latestLatLon = Some(xtractor.extractFromIntent(intent))
        }
    }
}