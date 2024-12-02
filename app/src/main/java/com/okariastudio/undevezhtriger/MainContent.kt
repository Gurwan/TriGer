package com.okariastudio.undevezhtriger

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.okariastudio.undevezhtriger.ui.nav.BottomNavBar
import com.okariastudio.undevezhtriger.ui.nav.NavigationGraph
import com.okariastudio.undevezhtriger.viewmodel.MainViewModel

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            mainViewModel = mainViewModel
        )
    }
}

