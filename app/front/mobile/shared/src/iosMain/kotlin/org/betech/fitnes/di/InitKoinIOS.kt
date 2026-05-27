package org.betech.fitnes.di

/**
 * Swift-friendly entry point. iOSApp.swift calls this once on launch so
 * Koin is started before any Compose screen tries to resolve a ViewModel.
 */
fun doInitKoin() {
    initKoin()
}
