package com.example.viewtest.ui.manualScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.saudigitus.support_module.R
import com.saudigitus.support_module.ui.SupportScreen.ErrorsViewModel
import com.saudigitus.support_module.ui.components.BasicApp
import com.saudigitus.support_module.ui.components.ErrorComponent
import com.saudigitus.support_module.ui.theme.app_blue_color

@Composable
fun ErrorsScreen(
    navController: NavHostController,
    onBack: () -> Unit,
) {
    val viewModel = hiltViewModel<ErrorsViewModel>()
    val errorsUiState by viewModel.errorsUiState.collectAsStateWithLifecycle()

    BasicApp(
        title = stringResource(id = R.string.sync_errors_view_title),
        onBack = onBack,
        fab = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send") }, // Icon on the FAB
                text = { Text(stringResource(id = R.string.fab_send_lb)) }, // Text on the FAB
                onClick = { /* Action when clicked */ },
                containerColor = app_blue_color, // Background color of the FAB
                contentColor = Color.White, // Color of the text and icon
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFFFFFFF))
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                Column {
                    Text(
                        text = (errorsUiState.errorsItems.size.toString()) + " " + stringResource(id = R.string.sync_errors),
                        fontSize = 16.sp,
                        color = Color.Gray,
                    )
                    Spacer(Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .height(400.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(errorsUiState.errorsItems) { error ->
                            ErrorComponent(
                                error = error.errorDescription.toString(),
                                type = error.errorCode.toString(),
                                comp = error.errorComponent ?: "",
                                date = error.creationDate.toString(),
                            )
                        }
                    }
                }

                val textState = remember { mutableStateOf("") }
                Spacer(Modifier.height(20.dp))
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text(text = stringResource(id = R.string.textarea_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 6,
                    maxLines = 6,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default,
                    ),
                )
                Spacer(Modifier.height(20.dp))
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    ErrorsScreen(navController = NavHostController(LocalContext.current), onBack = {})
}
