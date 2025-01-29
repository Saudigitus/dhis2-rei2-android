package org.saudigitus.rei.navigator

import android.content.Context
import android.content.Intent

interface TeiDashboardComponentProvider {
    fun launch(
        context: Context,
        teiUid: String?,
        programUid: String?,
        enrollmentUid: String?,
    ): Intent
}
