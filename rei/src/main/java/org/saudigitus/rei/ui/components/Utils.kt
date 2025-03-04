package org.saudigitus.rei.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentManager
import org.dhis2.commons.orgunitselector.OUTreeFragment
import org.dhis2.commons.orgunitselector.OrgUnitSelectorScope
import org.dhis2.ui.Dhis2ProgressIndicator
import org.hisp.dhis.mobile.ui.designsystem.component.ExtendedFAB
import org.hisp.dhis.mobile.ui.designsystem.component.FAB
import org.hisp.dhis.mobile.ui.designsystem.component.FABStyle
import org.hisp.dhis.mobile.ui.designsystem.theme.TextColor
import org.saudigitus.rei.R
import org.saudigitus.rei.data.model.OU

@Composable
fun NoResults() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_empty_folder),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(R.string.no_data_to_display),
            fontSize = 17.sp,
            color = Color.Black.copy(alpha = 0.38f),
            style = LocalTextStyle.current.copy(
                lineHeight = 24.sp,
                fontFamily = FontFamily(Font(R.font.rubik_regular)),
            ),
        )
    }
}

@Composable
fun LoadingContent(loadingDescription: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Dhis2ProgressIndicator(loadingDescription)
    }
}

@ExperimentalAnimationApi
@Composable
fun CreateNewButton(
    teTypeName: String,
    modifier: Modifier = Modifier,
    extended: Boolean = true,
    onClick: () -> Unit,
) {
    val icon = @Composable {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.Add,
            contentDescription = "",
            tint = TextColor.OnPrimaryContainer,
        )
    }

    AnimatedContent(
        targetState = extended,
        label = "FAB_Expansion",
    ) {
        if (it) {
            ExtendedFAB(
                modifier = modifier,
                onClick = onClick,
                text = stringResource(R.string.search_new_te_type, teTypeName.lowercase()),
                icon = icon,
                style = FABStyle.SECONDARY,
            )
        } else {
            FAB(
                modifier = modifier,
                onClick = onClick,
                icon = icon,
                style = FABStyle.SECONDARY,
            )
        }
    }
}

fun launchOuTreeSelector(
    supportFragmentManager: FragmentManager,
    selectedOrgUnit: OU? = null,
    program: String,
    onOrgUnitSelected: (ou: OU) -> Unit,
) {
    OUTreeFragment.Builder()
        .singleSelection()
        .orgUnitScope(OrgUnitSelectorScope.ProgramCaptureScope(program))
        .withPreselectedOrgUnits(
            selectedOrgUnit?.let { listOf(it.uid) } ?: emptyList(),
        )
        .onSelection { selectedOrgUnits ->
            val selectedOU = selectedOrgUnits.firstOrNull()
            if (selectedOU != null) {
                onOrgUnitSelected(
                    OU(
                        uid = selectedOU.uid(),
                        displayName = selectedOU.displayName(),
                    ),
                )
            }
        }
        .build()
        .show(supportFragmentManager, "OU_TREE")
}
