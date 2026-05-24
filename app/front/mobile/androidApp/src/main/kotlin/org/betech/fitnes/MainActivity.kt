package org.betech.fitnes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Dev-only deep-link plumbing: adb shell am start -W -a android.intent.action.VIEW \
        //   -d "fitnes://app?devScreen=q1-goal" org.betech.fitnes
        val devScreen: String? = intent?.data?.getQueryParameter("devScreen")

        setContent {
            App(devScreen = devScreen)
        }
    }
}
