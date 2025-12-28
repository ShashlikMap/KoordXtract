package com.shashlikmap.koordxtractdemo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val koordAppLogic = AppLogic()

@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            ) {
                LatLonResult(koordAppLogic)
            }
        }
    }
}

@Composable
private fun BoxScope.LatLonResult(appLogic: AppLogic) {
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            appLogic.linksTest()
        }) {
            Text("Start Links Test")
        }

        Spacer(modifier = Modifier.height(48.dp))

        appLogic.latestLatLon.fold({
            Text(text = "Or share location manually")
        }, { result ->
            when (result) {
                AppLogic.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AppLogic.State.Result -> {
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
