package com.okariastudio.triger.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.okariastudio.triger.ArventennouScreen
import com.okariastudio.triger.BrezhodexScreen
import com.okariastudio.triger.DeskinScreen
import com.okariastudio.triger.QuizScreen
import com.okariastudio.triger.QuizWriteScreen
import com.okariastudio.triger.viewmodel.MainViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    NavHost(navController, startDestination = "deskiñ", modifier = modifier) {
        composable("deskiñ") {
            DeskinScreen(mainViewModel, navController)
        }
        composable("brezhodex") {
            BrezhodexScreen(mainViewModel, navController)
        }
        composable("arventennoù") {
            ArventennouScreen()
        }
        composable("quizChoose") {
            val quizItem by mainViewModel.currentQuizItem.collectAsState()

            quizItem?.let {
                QuizScreen(
                    quizItem = it,
                    onNext = {
                        navController.navigate("quizWrite")
                    }
                )
            } ?: run {
                navController.popBackStack()
            }
        }
        composable("quizWrite") {
            val quizItem by mainViewModel.currentQuizItem.collectAsState()

            quizItem?.let {
                QuizWriteScreen(
                    quizItem = it,
                    onNext = {
                        mainViewModel.validateQuiz(it)
                        navController.navigate("brezhodex")
                    }
                )
            } ?: run {
                navController.navigate("deskiñ")
            }
        }
    }
}