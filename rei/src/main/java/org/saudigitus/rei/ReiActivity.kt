package org.saudigitus.rei

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.dhis2.commons.locationprovider.LocationProvider
import org.dhis2.commons.sync.SyncContext
import org.dhis2.commons.sync.SyncDialog
import org.dhis2.form.ui.provider.FormResultDialogProvider
import org.dhis2.ui.theme.Dhis2Theme
import org.saudigitus.rei.navigator.ReiNavigator
import org.saudigitus.rei.ui.EnrollmentFormScreen
import org.saudigitus.rei.ui.HomeRoute
import org.saudigitus.rei.ui.HomeScreen
import org.saudigitus.rei.ui.HomeViewModel
import org.saudigitus.rei.ui.components.launchOuTreeSelector
import org.saudigitus.rei.ui.theme.ReiTheme
import javax.inject.Inject

@AndroidEntryPoint
class ReiActivity : FragmentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var enrollmentResultDialogProvider: FormResultDialogProvider

    @Inject
    lateinit var locationProvider: LocationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            ReiTheme(
                dynamicColor = false,
                darkTheme = false,
            )  {
                viewModel.setBundle(intent?.extras)
                val uiState by viewModel.uiState.collectAsState()

                NavHost(
                    navController,
                    startDestination = HomeRoute.HOME,
                ) {
                    composable(HomeRoute.HOME) {
                        HomeScreen(
                            activity = this@ReiActivity,
                            uiState = uiState,
                            onSync = ::syncProgram,
                            loadStageData = viewModel::loadStageData,
                            onTeiClick = ::navToTeiDashboard,
                            onStageItem = viewModel::loadTEI,
                            onNewEnrollment = {
                                launchOuTreeSelector(
                                    supportFragmentManager = this@ReiActivity.supportFragmentManager,
                                    selectedOrgUnit = null,
                                    program = viewModel.program.value,
                                    onOrgUnitSelected = { ou ->
                                        viewModel.generateEnrollment(ou)
                                    },
                                )
                            }
                        ) {
                            finish()
                        }
                    }
                     composable(HomeRoute.ENROLLMENT_FORM) {
                         EnrollmentFormScreen(
                             activity = this@ReiActivity,
                             enrollmentResultDialogProvider = enrollmentResultDialogProvider,
                             locationProvider = locationProvider,
                             enrollmentUid = "",
                         ) {
                            navController.navigateUp()
                         }
                     }
                }
            }
        }
    }

    private fun syncProgram() {
        SyncDialog(
            activity = this@ReiActivity,
            recordUid = viewModel.program.value,
            syncContext = SyncContext.TrackerProgram(viewModel.program.value),
            onNoConnectionListener = {
                Snackbar.make(
                    this.window.decorView.rootView,
                    getString(R.string.sync_offline_check_connection),
                    Snackbar.LENGTH_SHORT,
                ).show()
            },
        ).show()
    }

    @Suppress("unused")
    private fun launchLineListing() {
        ReiNavigator(
            activity = this@ReiActivity,
            intent?.extras!!,
        ).navigateToLineListing()
    }

    private fun navToTeiDashboard(
        teiUid: String?,
        enrollmentUid: String?,
    ) {
        ReiNavigator(this@ReiActivity)
            .navigateToTeiDashboard(
                teiUid,
                viewModel.program.value,
                enrollmentUid,
            )
    }
}
