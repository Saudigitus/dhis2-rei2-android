package com.saudigitus.support_module.ui

import AppNavHost
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.saudigitus.support_module.R
import com.saudigitus.support_module.ui.components.CustomCard
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.saudigitus.support_module.MainActivity
import com.saudigitus.support_module.utils.Constants

@Composable
fun MenuScreen(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(color = Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First Card
            CustomCard(
                imageResId = R.drawable.manual_icon,
                title = stringResource(id = R.string.manuals),
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra(Constants.SCREENS_KEY, Screen.Manuals.route)
                    context.startActivity(intent)
                }
            )
            // Second Card
            CustomCard(
                imageResId = R.drawable.support,
                title =stringResource(id =  R.string.support),
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra(Constants.SCREENS_KEY, Screen.Support.route)
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    MenuScreen(context = LocalContext.current)
}