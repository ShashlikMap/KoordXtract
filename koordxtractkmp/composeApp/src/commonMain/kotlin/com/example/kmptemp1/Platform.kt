package com.example.kmptemp1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform