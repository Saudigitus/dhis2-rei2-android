package org.saudigitus.rei.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.hisp.dhis.mobile.ui.designsystem.theme.SurfaceColor
import org.saudigitus.rei.data.model.Stage

@Stable
data class StageTabState(
    val stages: List<Stage> = emptyList(),
    val stagesData: List<Triple<String, String, Color>> = emptyList(),
)

@Composable
fun StageTab(
    modifier: Modifier = Modifier,
    state: StageTabState = StageTabState(),
    onAction: (uid: String) -> Unit,
) {
    var tabState by remember { mutableIntStateOf(0) }

    if (state.stages.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            ScrollableTabRow(
                selectedTabIndex = tabState,
                containerColor = Color.White,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .tabIndicatorOffset(tabPositions[tabState])
                            .background(Color.Transparent, shape = RoundedCornerShape(4.dp)),
                        color = SurfaceColor.Primary,
                    )
                },
            ) {
                state.stages.forEachIndexed { index, stage ->
                    Tab(
                        selected = tabState == index,
                        onClick = {
                            tabState = index
                            onAction(stage.uid)
                        },
                        text = {
                            Text(
                                text = stage.displayName ?: "",
                                maxLines = 1,
                                softWrap = true,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if(state.stagesData.size >= 3) {
                    Arrangement.SpaceBetween
                } else { Arrangement.SpaceEvenly },
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(state.stagesData) {
                    HomeStageCard(
                        modifier = Modifier.size(120.dp),
                        state = HomeStageCardState(
                            title = it.first.ifEmpty { "0" },
                            subtitle = it.second,
                            bottomColor = it.third,
                        ),
                    )
                }
            }
        }
    }
}
