package org.saudigitus.rei.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.hisp.dhis.mobile.ui.designsystem.component.navigationBar.NavigationBar
import org.hisp.dhis.mobile.ui.designsystem.component.navigationBar.NavigationBarItem


enum class NavigationItem {
    REI,
    ANALYTICS,
    NONE,
}

private val navItems = listOf(
    NavigationBarItem(
        id = NavigationItem.REI.ordinal,
        icon = Icons.Default.Dashboard,
        label = "REI"
    ),
    NavigationBarItem(
        id = NavigationItem.ANALYTICS.ordinal,
        icon = Icons.Default.BarChart,
        label = "Charts"
    )
)

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    destination: Int = NavigationItem.REI.ordinal,
    onItemClick: (Int) -> Unit = {},
) {
    NavigationBar(
        modifier = modifier,
        items = navItems,
        selectedItemIndex = destination,
    ) {
        onItemClick(it)
    }
}