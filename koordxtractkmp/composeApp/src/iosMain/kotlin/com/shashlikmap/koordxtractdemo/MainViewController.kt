package com.shashlikmap.koordxtractdemo

import androidx.compose.ui.window.ComposeUIViewController

@Suppress("unused")
fun handlingUrlFromiOS(url: String) = koordAppLogic.handlingData(url)

@Suppress("unused")
fun MainViewController() = ComposeUIViewController { App() }
