package com.shashlikmap.koordxtractdemo

import androidx.compose.ui.window.ComposeUIViewController

fun handlingUrlFromiOS(url: String) = koordAppLogic.handlingData(url)

fun MainViewController() = ComposeUIViewController { App() }