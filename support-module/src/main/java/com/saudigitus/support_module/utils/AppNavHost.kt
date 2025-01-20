import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.viewtest.ui.manualScreen.ErrorsScreen
import com.saudigitus.support_module.ui.MenuScreen
import com.saudigitus.support_module.ui.Screen
import com.saudigitus.support_module.ui.manualScreen.GeneralReportScreen
import com.saudigitus.support_module.ui.manualScreen.ManualScreen
import com.saudigitus.support_module.ui.manualScreen.PdfViewer
import timber.log.Timber
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHost(navController: NavHostController, route: String, activity: Activity) {
    NavHost(
        navController = navController,
        startDestination = route,
    ) {
        composable(Screen.Menu.route) {
            MenuScreen(context = LocalContext.current)
        }
        composable(Screen.Manuals.route) {
            ManualScreen(navController = navController, onBack = {
                activity.finish()
            })
        }
        composable(Screen.Support.route) {
            SupportScreen(navController = navController, onBack = {
                activity.finish()
            })
        }
        composable(Screen.SyncErrors.route) {
            ErrorsScreen(navController = navController, onBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.GeneralErrors.route) {
            GeneralReportScreen(navController = navController, onBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.ViewPdf.route) { backStackEntry ->
            val encodedPath = backStackEntry.arguments?.getString("path")
            val filePath = encodedPath?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            val file = if (filePath != null) File(filePath) else null
            PdfViewer(navController = navController, file = file, onBack = {
                navController.popBackStack()
            })
        }
    }
}

