package com.pixnpunk.quoroscope

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform