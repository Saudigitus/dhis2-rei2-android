package org.saudigitus.rei.navigator

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ReiNavigator(
    val activity: FragmentActivity,
    val bundle: Bundle,
) {
    fun navigateToLineListing() {
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
