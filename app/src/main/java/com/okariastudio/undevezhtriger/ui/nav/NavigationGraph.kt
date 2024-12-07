package com.okariastudio.undevezhtriger.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.okariastudio.undevezhtriger.ArventennouScreen
import com.okariastudio.undevezhtriger.BrezhodexScreen
import com.okariastudio.undevezhtriger.DeskinScreen
import com.okariastudio.undevezhtriger.viewmodel.MainViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    NavHost(navController, startDestination = "deskiñ", modifier = modifier) {
        composable("deskiñ") {
            DeskinScreen(mainViewModel)
        }
        composable("brezhodex") {
            BrezhodexScreen(mainViewModel)
        }
        composable("arventennoù") {
            ArventennouScreen()
        }
    }
}