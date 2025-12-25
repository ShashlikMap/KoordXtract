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
import com.shashlik.koordxtract.LatLonExtractor
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var latestLatLon by mutableStateOf<LatLon?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlingIntent(intent)
        enableEdgeToEdge()
        setContent {
            KoordXtractTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(text = "LatLon: $latestLatLon")
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
            latestLatLon = LatLonExtractor.extractFromIntent(intent)
        }
    }
}