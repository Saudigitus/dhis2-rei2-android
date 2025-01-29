package org.saudigitus.rei

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
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
import org.saudigitus.rei.ui.mapper.TEICardMapper
import org.saudigitus.rei.ui.stages.StageViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ReiActivity : FragmentActivity() {

    private val viewModel: StageViewModel by viewModels()
    private val homeviewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var teiCardMapper: TEICardMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Dhis2Theme {
                viewModel.setBundle(intent?.extras)

                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(
                        activity = this@ReiActivity,
                        teiCardMapper = teiCardMapper,
                        onSync = ::syncProgram,
                        onNext = ::launchLineListing,
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


}
