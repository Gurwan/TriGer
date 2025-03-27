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
import com.okariastudio.triger.data.firebase.Tracking
import com.okariastudio.triger.data.model.QuizLimit
import com.okariastudio.triger.data.model.QuizType
import com.okariastudio.triger.ui.screens.ArventennouScreen
import com.okariastudio.triger.ui.screens.BrezhodexScreen
import com.okariastudio.triger.ui.screens.DeskinScreen
import com.okariastudio.triger.ui.screens.QuizChooseScreen
import com.okariastudio.triger.ui.screens.QuizStartScreen
import com.okariastudio.triger.ui.screens.QuizSummaryScreen
import com.okariastudio.triger.ui.screens.QuizWriteScreen
import com.okariastudio.triger.viewmodel.GerViewModel
import com.okariastudio.triger.viewmodel.QuizViewModel
import com.okariastudio.triger.viewmodel.SettingsViewModel
import kotlin.random.Random

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel,
    settingsViewModel: SettingsViewModel,
    gerViewModel: GerViewModel
) {
    NavHost(navController, startDestination = "deskiñ", modifier = modifier) {

        composable("deskiñ") {
            DeskinScreen(gerViewModel, quizViewModel, navController)
        }

        composable("brezhodex") {
            val tracking = Tracking(context = navController.context)
            BrezhodexScreen(gerViewModel, quizViewModel, navController, tracking)
        }

        composable("arventennoù") {
            ArventennouScreen(settingsViewModel, navController)
        }

        composable("quiz") {
            LaunchedEffect(Unit) {
                quizViewModel.fetchTotalGeriouLearned()
            }

            QuizStartScreen(quizViewModel, navController)
        }

        composable("quizChoose") {
            val quizItem by quizViewModel.currentQuizItem.collectAsState()
            val quizSettings by quizViewModel.currentQuizSettings.observeAsState()

            quizItem?.let {
                QuizChooseScreen(
                    quizItem = it,
                    stop = {
                        navController.navigate("quizSummary")
                    },
                    unlimitedQuiz = quizSettings?.limit == QuizLimit.NO_LIMIT,
                    onNext = {
                        if (quizSettings != null) {
                            when (quizViewModel.loopQuiz()) {
                                false -> {
                                    navController.navigate("quizSummary")
                                }

                                true -> {
                                    if (quizSettings?.limit == QuizLimit.NO_LIMIT || (quizSettings?.limit == QuizLimit.N_WORDS && quizSettings!!.score <= quizSettings!!.limitValue)) {
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
                                        navController.navigate("quizSummary")
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
            val quizItem by quizViewModel.currentQuizItem.collectAsState()
            val quizSettings by quizViewModel.currentQuizSettings.observeAsState()
            val tracking = Tracking(context = navController.context)

            quizItem?.let {
                QuizWriteScreen(
                    quizItem = it,
                    stop = {

                        navController.navigate("quizSummary")
                    },
                    unlimitedQuiz = quizSettings?.limit == QuizLimit.NO_LIMIT,
                    onNext = {
                        quizViewModel.validateQuiz(it)
                        if (quizSettings != null) {
                            when (quizViewModel.loopQuiz()) {
                                false -> {
                                    navController.navigate("quizSummary")
                                }

                                true -> {
                                    if (quizSettings?.limit == QuizLimit.NO_LIMIT || (quizSettings?.limit == QuizLimit.N_WORDS && quizSettings!!.score <= quizSettings!!.limitValue)) {
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
                                        navController.navigate("quizSummary")
                                    }
                                }
                            }
                        } else {
                            it.exactWord?.breton?.let { ger -> tracking.logGerLearned(ger) }
                            navController.navigate("brezhodex")
                        }
                    }
                )
            } ?: run {
                navController.navigate("deskiñ")
            }
        }

        composable("quizSummary") {
            QuizSummaryScreen(quizViewModel, navController)
        }
    }
}