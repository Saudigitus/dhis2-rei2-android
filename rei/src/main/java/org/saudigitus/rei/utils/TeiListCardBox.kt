package org.saudigitus.rei.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.hisp.dhis.mobile.ui.designsystem.theme.Radius

@Composable
fun TeiListCardBox(
    modifier: Modifier = Modifier,
    listCard: @Composable () -> Unit,
) {
    Box {
        listCard()

        Box(modifier = Modifier.clip(shape = RoundedCornerShape(Radius.XS)).then(modifier))
    }
}
