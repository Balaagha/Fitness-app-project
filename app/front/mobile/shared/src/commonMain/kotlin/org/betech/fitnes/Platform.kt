package org.betech.fitnes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform