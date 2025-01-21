package org.saudigitus.rei.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.hisp.dhis.mobile.ui.designsystem.theme.Radius

@Composable
fun TeiListCardBox(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    listCard: @Composable () -> Unit,
) {
    Box {
        listCard()

        Box(modifier = Modifier.clip(shape = RoundedCornerShape(Radius.XS)).then(modifier))
        Text(
            modifier = Modifier.align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            overflow = TextOverflow.Ellipsis,
            softWrap = true,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            color = color,
            text = text
        )
    }
}
