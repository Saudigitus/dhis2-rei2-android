import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.saudigitus.support_module.R
import com.saudigitus.support_module.ui.Screen
import com.saudigitus.support_module.ui.components.BasicApp
import com.saudigitus.support_module.ui.components.SimpleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    navController: NavHostController,
    onBack: () -> Unit = {}, // Placeholder for back action
) {
    BasicApp(
        title = stringResource(id = R.string.support_view_title),
        onBack = onBack,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.support_view_subtitle),
                    fontSize = 16.sp,
                    color = Color.Gray,
                )
                Spacer(Modifier.height(20.dp))
                SimpleCard(
                    title = stringResource(
                        id = R.string.sync_errord,
                    ),
                    icon = Icons.Default.Close,
                    onClick = {
                        navController.navigate(Screen.SyncErrors.route)
                    },
                )
                Spacer(Modifier.height(20.dp))
                SimpleCard(
                    title = stringResource(
                        id = R.string.other_errors,
                    ),
                    icon = Icons.Default.Warning,
                    onClick = {
                        navController.navigate(Screen.GeneralErrors.route)
                    },
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun SupportPreview() {
    SupportScreen(navController = NavHostController(LocalContext.current), onBack = {})
}
