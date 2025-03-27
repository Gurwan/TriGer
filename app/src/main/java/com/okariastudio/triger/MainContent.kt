package com.okariastudio.triger

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.okariastudio.triger.ui.nav.BottomNavBar
import com.okariastudio.triger.ui.nav.NavigationGraph
import com.okariastudio.triger.viewmodel.GerViewModel
import com.okariastudio.triger.viewmodel.QuizViewModel
import com.okariastudio.triger.viewmodel.SettingsViewModel

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel,
    settingsViewModel: SettingsViewModel,
    gerViewModel: GerViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            quizViewModel = quizViewModel,
            settingsViewModel = settingsViewModel,
            gerViewModel = gerViewModel
        )
    }
}

