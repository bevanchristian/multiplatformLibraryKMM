package com.example.libbevantestingkmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform