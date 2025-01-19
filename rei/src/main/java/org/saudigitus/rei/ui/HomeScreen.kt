package org.saudigitus.rei.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saudigitus.support_module.ui.MenuScreen
import org.saudigitus.rei.R
import org.saudigitus.rei.ui.components.Toolbar
import org.saudigitus.rei.ui.components.ToolbarActionState
import org.saudigitus.rei.ui.stages.StageTab
import org.saudigitus.rei.ui.stages.StageViewModel
import org.saudigitus.rei.ui.theme.seed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: StageViewModel = hiltViewModel(),
    onSync: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val config by viewModel.config.collectAsStateWithLifecycle()
    val toolbarHeaders by viewModel.toolbarHeaders.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Toolbar(
                headers = toolbarHeaders,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = seed,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),
                navigationAction = onBack,
                disableNavigation = false,
                actionState = ToolbarActionState(filterVisibility = false),
                syncAction = onSync,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(R.string.patients),
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        softWrap = true,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                    )
                },
                onClick = onNext::invoke,
                contentColor = Color.White,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            if (config?.defaults?.displayStages == true) {
                StageTab(
                    modifier = Modifier.fillMaxWidth(),
                    state = state,
                    onAction = viewModel::loadStageData,
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            if (config?.defaults?.displaySupport == true) {
                Text(
                    text = "Suporte ao utilizador",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(.5f),
                )

                MenuScreen(context = context)
            }
        }
    }
}
