package org.saudigitus.rei.navigator

import android.content.Context
import android.content.Intent
import android.os.Bundle

interface LineListingComponentProvider {
    fun launch(
        context: Context,
        bundle: Bundle,
    ): Intent
}
