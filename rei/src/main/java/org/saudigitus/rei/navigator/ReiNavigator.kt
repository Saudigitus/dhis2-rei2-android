package org.saudigitus.rei.navigator

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ReiNavigator(
    val activity: FragmentActivity,
    val bundle: Bundle? = null,
) {
    fun navigateToLineListing() {
        if (bundle != null) {
            (activity.applicationContext as? NavigatorComponentProvider)
                ?.lineListing
                ?.launch(
                    activity,
                    bundle,
                )?.let {
                    activity.startActivity(it)
                }
        }
    }

    fun navigateToTeiDashboard(
        teiUid: String?,
        programUid: String?,
        enrollmentUid: String?,
    ) {
        (activity.applicationContext as? NavigatorComponentProvider)
            ?.teiDashboard
            ?.launch(
                activity,
                teiUid,
                programUid,
                enrollmentUid,
            )?.let {
                activity.startActivity(it)
            }
    }
}
