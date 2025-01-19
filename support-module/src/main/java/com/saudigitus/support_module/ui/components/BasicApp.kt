package com.saudigitus.support_module.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saudigitus.support_module.ui.theme.app_blue_color

@JvmOverloads
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicApp(
    title: String,
    content: @Composable () -> Unit,
    onBack: () -> Unit = {} ,// Placeholder for back action
    fab:  (@Composable () -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = app_blue_color // Blue background color
                )
            )
        },
        floatingActionButton = {
           fab?.let {
               it()
           }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(Color(0xFF2196F3)) // Set your background color here
                .padding(paddingValues) // Ensures the padding is applied to the content
        ) {
            // Wrapping the content with a Surface to give it rounded corners
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), // Rounded top corners
                shadowElevation = 4.dp // Elevation for shadow
            ) {
                // CONTENT
                content()
            }
        }
    }
}