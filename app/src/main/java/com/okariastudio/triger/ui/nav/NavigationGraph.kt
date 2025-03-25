package com.okariastudio.triger.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.okariastudio.triger.ArventennouScreen
import com.okariastudio.triger.BrezhodexScreen
import com.okariastudio.triger.DeskinScreen
import com.okariastudio.triger.QuizScreen
import com.okariastudio.triger.QuizStart
import com.okariastudio.triger.QuizSummary
import com.okariastudio.triger.QuizWriteScreen
import com.okariastudio.triger.data.firebase.Tracking
import com.okariastudio.triger.data.model.QuizLimit
import com.okariastudio.triger.data.model.QuizType
import com.okariastudio.triger.viewmodel.MainViewModel
import kotlin.random.Random

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
            val tracking = Tracking(context = navController.context)
            BrezhodexScreen(mainViewModel, navController, tracking)
        }
        composable("arventennoù") {
            ArventennouScreen(mainViewModel, navController)
        }
        composable("quiz") {
            LaunchedEffect(Unit) {
                mainViewModel.fetchTotalGeriouLearned()
            }

            QuizStart(mainViewModel, navController)
        }
        composable("quizChoose") {
            val quizItem by mainViewModel.currentQuizItem.collectAsState()
            val quizSettings by mainViewModel.currentQuizSettings.observeAsState()

            quizItem?.let {
                QuizScreen(
                    quizItem = it,
                    stop = {
                        mainViewModel.finishQuiz()
                        navController.navigate("quizSummary")
                    },
                    unlimitedQuiz = quizSettings?.limit == QuizLimit.NO_LIMIT,
                    onNext = {
                        if (quizSettings != null) {
                            when (mainViewModel.loopQuiz()) {
                                false -> {
                                    mainViewModel.finishQuiz()
                                    navController.navigate("quizSummary")
                                }

                                true -> {
                                    if (quizSettings?.limit == QuizLimit.NO_LIMIT || (quizSettings?.limit == QuizLimit.N_WORDS && quizSettings!!.score < quizSettings!!.limitValue)) {
                                        if (quizSettings!!.type == QuizType.CHOICE) {
                                            navController.navigate("quizChoose")
                                        } else {
                                            if (Random.nextBoolean()) {
                                                navController.navigate("quizWrite")
                                            } else {
                                                navController.navigate("quizChoose")
                                            }
                                        }
                                    } else if (quizSettings!!.score == quizSettings!!.limitValue) {
                                        mainViewModel.finishQuiz()
                                        navController.navigate("brezhodex")
                                    }
                                }
                            }
                        } else {
                            navController.navigate("quizWrite")
                        }
                    }
                )
            } ?: run {
                navController.popBackStack()
            }
        }
        composable("quizWrite") {
            val quizItem by mainViewModel.currentQuizItem.collectAsState()
            val quizSettings by mainViewModel.currentQuizSettings.observeAsState()

            quizItem?.let {
                QuizWriteScreen(
                    quizItem = it,
                    stop = {
                        mainViewModel.finishQuiz()
                        navController.navigate("quizSummary")
                    },
                    unlimitedQuiz = quizSettings?.limit == QuizLimit.NO_LIMIT,
                    onNext = {
                        mainViewModel.validateQuiz(it)
                        if (quizSettings != null) {
                            when (mainViewModel.loopQuiz()) {
                                false -> {
                                    mainViewModel.finishQuiz()
                                    navController.navigate("quizSummary")
                                }

                                true -> {
                                    if (quizSettings?.limit == QuizLimit.NO_LIMIT || (quizSettings?.limit == QuizLimit.N_WORDS && quizSettings!!.score < quizSettings!!.limitValue)) {
                                        if (quizSettings!!.type == QuizType.WRITE) {
                                            navController.navigate("quizWrite")
                                        } else {
                                            if (Random.nextBoolean()) {
                                                navController.navigate("quizWrite")
                                            } else {
                                                navController.navigate("quizChoose")
                                            }
                                        }
                                    } else if (quizSettings!!.score == quizSettings!!.limitValue) {
                                        mainViewModel.finishQuiz()
                                        navController.navigate("brezhodex")
                                    }
                                }
                            }
                        } else {
                            navController.navigate("brezhodex")
                        }
                    }
                )
            } ?: run {
                navController.navigate("deskiñ")
            }
        }
        composable("quizSummary") {
            QuizSummary(mainViewModel, navController)
        }
    }
}