package org.betech.fitnes.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Platform-specific Koin bindings (Android context, Darwin engine, etc.).
 */
expect fun platformModule(): Module

/**
 * Single entry point to initialise Koin from both Android Application and iOS.
 * `extra` lets the host inject platform glue (e.g. `androidContext(...)`).
 */
fun initKoin(extra: KoinAppDeclaration? = null) {
    startKoin {
        extra?.invoke(this)
        modules(
            domainModule,
            dataModule,
            presentationModule,
            platformModule()
        )
    }
}
