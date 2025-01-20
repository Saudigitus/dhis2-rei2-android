package org.saudigitus.rei.utils

import org.saudigitus.rei.R
import org.saudigitus.rei.ui.theme.Light_Error
import org.saudigitus.rei.ui.theme.Light_Success
import org.saudigitus.rei.ui.theme.Light_Warning

object HardcodeData {

    fun getHomeTabItemData() = listOf(
        Pair(R.string.effective, Light_Success),
        Pair(R.string.pending, Light_Warning),
        Pair(R.string.late, Light_Error),
    )
}
