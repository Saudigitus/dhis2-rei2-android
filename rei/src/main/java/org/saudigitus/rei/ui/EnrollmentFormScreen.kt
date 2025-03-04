package org.saudigitus.rei.ui

import android.widget.FrameLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import org.dhis2.commons.locationprovider.LocationProvider
import org.dhis2.form.model.EnrollmentMode
import org.dhis2.form.model.EnrollmentRecords
import org.dhis2.form.ui.FormView
import org.dhis2.form.ui.provider.FormResultDialogProvider

@Composable
fun EnrollmentFormScreen(
    activity: FragmentActivity,
    enrollmentUid: String,
    enrollmentResultDialogProvider: FormResultDialogProvider,
    locationProvider: LocationProvider,
    onFinish: () -> Unit,
) {
    Scaffold { innerPadding ->

        var frameId = 0
        AndroidView(
            modifier = Modifier.padding(innerPadding   ),
            factory = { context ->
                FrameLayout(context).apply {
                    id = ViewCompat.generateViewId()
                    frameId = id
                }
            },
            update = {
                val supportFragmentManager = activity.supportFragmentManager

                supportFragmentManager.beginTransaction()
                    .add(
                        enrollmentForm(
                            supportFragmentManager,
                            enrollmentUid,
                            enrollmentResultDialogProvider,
                            locationProvider,
                            onFinish = onFinish
                        ), "NEW_ENROLLMENT"
                    )
                    .commit()
            }
        )
    }
}


private fun enrollmentForm(
    supportFragmentManager: FragmentManager,
    enrollmentUid: String,
    enrollmentResultDialogProvider: FormResultDialogProvider,
    locationProvider: LocationProvider,
    onFinish: () -> Unit,
) = FormView.Builder()
    .locationProvider(locationProvider)
    .onFinishDataEntry(onFinish)
    .eventCompletionResultDialogProvider(enrollmentResultDialogProvider)
    .factory(supportFragmentManager)
    .setRecords(
        EnrollmentRecords(
            enrollmentUid = enrollmentUid,
            enrollmentMode = EnrollmentMode.NEW,
        )
    ).build()