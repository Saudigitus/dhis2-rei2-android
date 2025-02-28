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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.dhis2.commons.sync.SyncContext
import org.dhis2.commons.sync.SyncDialog
import org.dhis2.ui.theme.Dhis2Theme
import org.saudigitus.rei.navigator.ReiNavigator
import org.saudigitus.rei.ui.HomeScreen
import org.saudigitus.rei.ui.HomeViewModel

@AndroidEntryPoint
class ReiActivity : FragmentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Dhis2Theme {
                viewModel.setBundle(intent?.extras)
                val uiState by viewModel.uiState.collectAsState()

                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(
                        activity = this@ReiActivity,
                        uiState = uiState,
                        onSync = ::syncProgram,
                        onNext = ::launchLineListing,
                        loadStageData = viewModel::loadStageData,
                        onTeiClick = ::navToTeiDashboard,
                    ) {
                        finish()
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
