package org.saudigitus.rei.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.mobile.ui.designsystem.component.ListCard
import org.hisp.dhis.mobile.ui.designsystem.component.ListCardTitleModel
import org.saudigitus.rei.R
import org.saudigitus.rei.data.model.AppConfigItem
import org.saudigitus.rei.data.model.SearchTeiModel
import org.saudigitus.rei.ui.components.CreateNewButton
import org.saudigitus.rei.ui.components.HelpDialog
import org.saudigitus.rei.ui.components.LoadingContent
import org.saudigitus.rei.ui.components.NavBar
import org.saudigitus.rei.ui.components.NavigationItem
import org.saudigitus.rei.ui.components.NoResults
import org.saudigitus.rei.ui.components.StageTab
import org.saudigitus.rei.ui.components.StageTabState
import org.saudigitus.rei.ui.components.Toolbar
import org.saudigitus.rei.ui.components.ToolbarActionState
import org.saudigitus.rei.ui.components.ToolbarHeaders
import org.saudigitus.rei.ui.mapper.TEICardMapper
import org.saudigitus.rei.ui.theme.seed
import org.saudigitus.rei.utils.map

@Stable
data class HomeUIState(
    val isLoading: Boolean = false,
    val isLoadingSection2: Boolean = false,
    val toolbarHeaders: ToolbarHeaders = ToolbarHeaders(""),
    val stageTabState: StageTabState? = null,
    val config: AppConfigItem? = null,
    val teiCardMapper: TEICardMapper? = null,
    val teis: List<SearchTeiModel> = emptyList(),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    activity: FragmentActivity,
    uiState: HomeUIState,
    onSync: () -> Unit,
    loadStageData: (stage: String) -> Unit,
    onTeiClick: (tei: String, enrollment: String) -> Unit,
    onStageItem: (stage: String, eventStatus: EventStatus) -> Unit,
    onNewEnrollment: (route: String) -> Unit,
    onBack: () -> Unit,
) {
    val navController = rememberNavController()
    var route by rememberSaveable { mutableStateOf(NavigationItem.REI) }

    var openHelpDialog by remember { mutableStateOf(false) }

    if (openHelpDialog) {
        HelpDialog(
            onDismissRequest = { openHelpDialog = false },
        )
    }

    Scaffold(
        topBar = {
            Toolbar(
                headers = uiState.toolbarHeaders,
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
                actions = {
                    IconButton(onClick = { openHelpDialog = !openHelpDialog }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = stringResource(R.string.help),
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            CreateNewButton(
                modifier = Modifier,
                extended = true,
                onClick = { onNewEnrollment.invoke(HomeRoute.ENROLLMENT_FORM) },
                teTypeName = "",
            )
        },
        bottomBar = {
            NavBar(destination = route.ordinal) {
                route = when (it) {
                    NavigationItem.REI.ordinal -> NavigationItem.REI
                    NavigationItem.ANALYTICS.ordinal -> NavigationItem.ANALYTICS
                    else -> NavigationItem.NONE
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = route.name,
        ) {
            composable(NavigationItem.REI.name) {
                HomeUI(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .then(modifier),
                    uiState = uiState,
                    onAction = loadStageData,
                    onTeiClick = onTeiClick,
                    onStageItem = onStageItem,
                )
            }
            composable(NavigationItem.ANALYTICS.name) {
                AnalyticsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .then(modifier),
                    activity = activity,
                )
            }
        }
    }
}

@Composable
fun HomeUI(
    modifier: Modifier = Modifier,
    uiState: HomeUIState,
    onAction: (uid: String) -> Unit,
    onTeiClick: (tei: String, enrollment: String) -> Unit,
    onStageItem: (stage: String, eventStatus: EventStatus) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        if (!uiState.isLoading) {
            if (uiState.config?.defaults?.displayStages == true && uiState.stageTabState != null) {
                StageTab(
                    modifier = Modifier.fillMaxWidth(),
                    state = uiState.stageTabState,
                    onAction = onAction,
                    onStageItem = onStageItem
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            if (!uiState.isLoadingSection2) {
                uiState.teiCardMapper?.let { cardMapper ->
                    TEIList(
                        teiCardMapper = cardMapper,
                        teis = uiState.teis,
                        onCardClick = onTeiClick,
                    )
                }
            } else {
                LoadingContent()
            }
        } else {
            LoadingContent()
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun TEIList(
    teiCardMapper: TEICardMapper,
    teis: List<SearchTeiModel>,
    onCardClick: (String, String) -> Unit = { _, _ -> },
) {
    Text(
        text = stringResource(R.string.scheduled_children),
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black.copy(.5f),
        fontFamily = FontFamily(Font(R.font.rubik_regular)),
    )

    if (teis.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(teis) { student ->
                val card = student.map(teiCardMapper, onCardClick = onCardClick)

                ListCard(
                    modifier = Modifier.testTag("TEI_ITEM"),
                    listAvatar = card.avatar,
                    title = ListCardTitleModel(text = card.title),
                    lastUpdated = card.lastUpdated,
                    additionalInfoList = card.additionalInfo,
                    actionButton = card.actionButton,
                    expandLabelText = card.expandLabelText,
                    shrinkLabelText = card.shrinkLabelText,
                    onCardClick = card.onCardCLick,
                )
            }
        }
    } else {
        NoResults()
    }
}