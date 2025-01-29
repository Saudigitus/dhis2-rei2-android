package com.saudigitus.support_module

import AppNavHost
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.saudigitus.support_module.ui.Screen
import com.saudigitus.support_module.ui.theme.SupportUiTheme
import com.saudigitus.support_module.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import org.dhis2.ui.theme.Dhis2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupportUiTheme(
                darkTheme = false,
                dynamicColor = false,
            ) {
                val screen = intent.extras?.getString(Constants.SCREENS_KEY) ?: Screen.Menu.route
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavHost(navController = navController, route = screen, activity = this)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Dhis2Theme {
        Greeting("Android")
    }
}
