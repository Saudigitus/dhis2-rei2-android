package org.saudigitus.rei.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.mobile.ui.designsystem.theme.Radius
import org.hisp.dhis.mobile.ui.designsystem.theme.dropShadow
import org.saudigitus.rei.utils.HardcodeData

@Stable
data class HomeStageCardState(
    val title: String,
    val subtitle: String,
    val bottomColor: Color,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeStageCard(
    modifier: Modifier,
    state: HomeStageCardState,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .dropShadow(shape = RoundedCornerShape(Radius.XS))
            .then(modifier),
        shape = RoundedCornerShape(Radius.XS),
        backgroundColor = state.bottomColor,
        onClick = onClick::invoke
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(.95f)
                    .background(color = Color.White),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = state.title,
                    color = state.bottomColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    maxLines = 1,
                )
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.LightGray.copy(0.25f),
                    thickness = 0.75.dp,
                )
                Text(
                    text = state.subtitle,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeStageCard(
    modifier: Modifier = Modifier,
    state: HomeStageCardState = HomeStageCardState(
        title = "30",
        subtitle = stringResource(HardcodeData.getHomeTabItemData()[0].first),
        bottomColor = HardcodeData.getHomeTabItemData()[0].second,
    ),
) {
    HomeStageCard(
        modifier = modifier.size(128.dp),
        state = state,
    )
}

@Preview
@Composable
fun PreviewHomeStageCardPending(
    modifier: Modifier = Modifier,
    state: HomeStageCardState = HomeStageCardState(
        title = "20",
        subtitle = stringResource(HardcodeData.getHomeTabItemData()[1].first),
        bottomColor = HardcodeData.getHomeTabItemData()[1].second,
    ),
) {
    HomeStageCard(
        modifier = modifier.size(128.dp),
        state = state,
    )
}

@Preview
@Composable
fun PreviewHomeStageCardLate(
    modifier: Modifier = Modifier,
    state: HomeStageCardState = HomeStageCardState(
        title = "60",
        subtitle = stringResource(HardcodeData.getHomeTabItemData()[2].first),
        bottomColor = HardcodeData.getHomeTabItemData()[2].second,
    ),
) {
    HomeStageCard(
        modifier = modifier.size(128.dp),
        state = state,
    )
}
