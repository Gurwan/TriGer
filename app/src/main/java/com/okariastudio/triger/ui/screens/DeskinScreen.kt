package com.okariastudio.triger.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.okariastudio.triger.R
import com.okariastudio.triger.ui.templates.GerList
import com.okariastudio.triger.viewmodel.GerViewModel
import com.okariastudio.triger.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeskinScreen(gerViewModel: GerViewModel, quizViewModel: QuizViewModel, navController: NavHostController) {
    val gersToday by gerViewModel.gersToday.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        gerViewModel.fetchGersForToday()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.tri_ger_du_jour))
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

            if (gersToday.isEmpty()) {
                item {
                    Text(
                        stringResource(id = R.string.no_ger_for_today),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                item {
                    GerList(
                        gersToday,
                        quizViewModel = quizViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}