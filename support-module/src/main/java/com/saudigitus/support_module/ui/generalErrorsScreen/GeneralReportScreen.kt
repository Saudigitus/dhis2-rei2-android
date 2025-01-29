package com.saudigitus.support_module.ui.manualScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.saudigitus.support_module.R
import com.saudigitus.support_module.ui.components.BasicApp
import com.saudigitus.support_module.ui.theme.Blue700
import com.saudigitus.support_module.ui.theme.app_blue_color

@Composable
fun GeneralReportScreen(
    navController: NavHostController,
    onBack: () -> Unit,
) {
    BasicApp(
        title = stringResource(id = R.string.general_errors_view_title),
        onBack = onBack,
        fab = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send") }, // Icon on the FAB
                text = { Text(stringResource(id = R.string.fab_send_lb)) }, // Text on the FAB
                onClick = { /* Action when clicked */ },
                containerColor = app_blue_color,
                contentColor = Color.White,
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.support),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.general_errors_view_lb),
                        fontSize = 12.sp,
                        color = Blue700,
                    )
                    Spacer(Modifier.height(10.dp))
                }
                val textState = remember { mutableStateOf("") }
                Spacer(Modifier.height(20.dp))
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text(text = stringResource(id = R.string.textarea_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 20,
                    maxLines = 20, // Allows up to 5 lines
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default, // Allows multiline input
                    ),
                )
                Spacer(Modifier.height(20.dp))
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun GenScreenPreview() {
    GeneralReportScreen(navController = NavHostController(LocalContext.current), onBack = {})
}
