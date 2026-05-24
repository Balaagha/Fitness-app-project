package org.betech.fitnes.localization

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun currentLocale(): String =
    NSLocale.currentLocale.languageCode
