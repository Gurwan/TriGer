package com.okariastudio.triger.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.okariastudio.triger.R
import com.okariastudio.triger.Utils
import com.okariastudio.triger.data.firebase.Tracking
import com.okariastudio.triger.data.model.SortOption
import com.okariastudio.triger.ui.templates.FilterRange
import com.okariastudio.triger.ui.templates.GerList
import com.okariastudio.triger.ui.templates.SortDropdown
import com.okariastudio.triger.viewmodel.GerViewModel
import com.okariastudio.triger.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrezhodexScreen(
    gerViewModel: GerViewModel,
    quizViewModel: QuizViewModel,
    navController: NavHostController,
    tracking: Tracking
) {
    val gersBrezhodex by gerViewModel.gersBrezhodex.observeAsState(emptyList())
    val gersBrezhodexDevezh by gerViewModel.gersBrezhodexDevezh.observeAsState(emptyList())
    val (filterValue, setFilterValue) = remember { mutableStateOf<IntRange?>(null) }
    val isMinimalView by gerViewModel.isMinMode.collectAsState()

    var isRangeDialogOpen by remember { mutableStateOf(false) }
    var isSortDialogOpen by remember { mutableStateOf(false) }
    var currentRange by remember { mutableStateOf(IntRange(-2, -1)) }
    var sortOption by remember { mutableStateOf(SortOption.DATE_NEWEST) }

    LaunchedEffect(Unit) {
        gerViewModel.fetchGersInBrezhodex()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.ton_brezhodex))
                },
                actions = {
                    IconButton(onClick = { isRangeDialogOpen = true }) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_filter),
                            contentDescription = stringResource(id = R.string.filter)
                        )
                    }
                    IconButton(onClick = { gerViewModel.toggleMinMode() }) {
                        if (isMinimalView) {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                contentDescription = stringResource(id = R.string.display)
                            )
                        } else {
                            Icon(
                                ImageVector.vectorResource(id = R.drawable.ic_app),
                                contentDescription = stringResource(id = R.string.display)
                            )
                        }
                    }
                    IconButton(onClick = { isSortDialogOpen = true }) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_sort),
                            contentDescription = stringResource(id = R.string.sort)
                        )
                    }
                    if (isSortDialogOpen && !isRangeDialogOpen) {
                        SortDropdown(
                            onDismiss = { isSortDialogOpen = false },
                            onApplySort = { option ->
                                sortOption = Utils.changeSortOption(sortOption, option)
                                tracking.logSortApply(option)
                                isSortDialogOpen = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (gersBrezhodexDevezh.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.ger_du_jour),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                item {
                    GerList(
                        gerList = gersBrezhodexDevezh,
                        quizViewModel = quizViewModel,
                        navController = navController,
                        minimal = isMinimalView
                    )
                }
            }

            val filteredGersBrezhodex = filterValue?.let { range ->
                gersBrezhodex.filter { ger -> ger.levelLearnings in range }
            } ?: gersBrezhodex

            val sortedGersBrezhodex = when (sortOption) {
                SortOption.BRETON_AZ -> filteredGersBrezhodex.sortedBy { it.breton }
                SortOption.BRETON_ZA -> filteredGersBrezhodex.sortedByDescending { it.breton }
                SortOption.LEVEL_ASC -> filteredGersBrezhodex.sortedBy { it.levelLearnings }
                SortOption.LEVEL_DESC -> filteredGersBrezhodex.sortedByDescending { it.levelLearnings }
                SortOption.DATE_NEWEST -> filteredGersBrezhodex.sortedByDescending { it.lastLearningDate }
                SortOption.DATE_OLDEST -> filteredGersBrezhodex.sortedBy { it.lastLearningDate }
                SortOption.FRANCAIS_AZ -> filteredGersBrezhodex.sortedBy { it.french }
                SortOption.FRANCAIS_ZA -> filteredGersBrezhodex.sortedByDescending { it.french }
                SortOption.LEVEL -> filteredGersBrezhodex.sortedBy { it.levelLearnings }
                SortOption.DATE -> filteredGersBrezhodex.sortedByDescending { it.lastLearningDate }
                SortOption.BRETON -> filteredGersBrezhodex.sortedBy { it.breton }
                SortOption.FRANCAIS -> filteredGersBrezhodex.sortedBy { it.french }
            }

            if (sortedGersBrezhodex.isNotEmpty()) {
                item {
                    Text(
                        text = if (sortedGersBrezhodex.isNotEmpty()) stringResource(id = R.string.autres_ger) else stringResource(
                            id = R.string.ger_appris
                        ),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                item {
                    GerList(
                        gerList = sortedGersBrezhodex,
                        quizViewModel = quizViewModel,
                        navController = navController,
                        minimal = isMinimalView
                    )
                }
            } else if (gersBrezhodexDevezh.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.no_ger_brezhodex),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }

        if (isRangeDialogOpen && !isSortDialogOpen) {
            FilterRange(
                gers = gersBrezhodex,
                onDismiss = { isRangeDialogOpen = false },
                currentRange = currentRange,
                onApplyRange = { range ->
                    currentRange = range
                    tracking.logFilterApply(range.first, range.last)
                    setFilterValue(range)
                    isRangeDialogOpen = false
                }
            )
        }
    }
}