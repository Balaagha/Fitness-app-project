package org.betech.fitnes.localization

import java.util.Locale

actual fun currentLocale(): String = Locale.getDefault().language
