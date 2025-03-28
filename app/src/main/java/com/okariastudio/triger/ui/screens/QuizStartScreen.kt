package com.okariastudio.triger.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.okariastudio.triger.R
import com.okariastudio.triger.data.model.QuizLimit
import com.okariastudio.triger.data.model.QuizTarget
import com.okariastudio.triger.data.model.QuizType
import com.okariastudio.triger.viewmodel.QuizViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizStartScreen(quizViewModel: QuizViewModel, navController: NavHostController) {
    var quizTarget by remember { mutableStateOf(QuizTarget.ALL_WORDS) }
    var quizLimitType by remember { mutableStateOf(QuizLimit.NO_LIMIT) }
    var sliderWordPosition by remember { mutableFloatStateOf(0f) }
    var quizType by remember { mutableStateOf(QuizType.BOTH) }

    val totalGeriouLearned by quizViewModel.totalGeriouLearned.observeAsState(initial = 10)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.quiz))
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

            item {
                Text(text = stringResource(id = R.string.target_quiz_label))
                QuizTarget.entries.forEach { target ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = quizTarget == target,
                            onClick = { quizTarget = target })
                        Text(target.value)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(text = stringResource(id = R.string.limit_quiz_label))
                QuizLimit.entries.forEach { type ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = quizLimitType == type,
                            onClick = { quizLimitType = type })
                        Text(type.value)
                    }
                }
            }

            when (quizLimitType) {
                QuizLimit.N_WORDS -> {
                    item {
                        Text(text = stringResource(id = R.string.words_limit_quiz))

                    }

                    item {
                        Slider(
                            value = sliderWordPosition,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.secondary,
                                activeTrackColor = MaterialTheme.colorScheme.secondary,
                                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            valueRange = 1f..totalGeriouLearned.toFloat(),
                            onValueChange = { sliderWordPosition = it }
                        )
                        Text(text = sliderWordPosition.toInt().toString())
                    }
                }

                else -> {}
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(text = stringResource(id = R.string.type_quiz_label))
            }

            QuizType.entries.forEach { type ->
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = quizType == type, onClick = { quizType = type })
                        Text(type.value)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Button(
                    onClick = {
                        quizViewModel.startQuiz(
                            quizType,
                            quizLimitType,
                            sliderWordPosition.toInt(),
                            quizTarget
                        )
                        if (quizType == QuizType.WRITE) {
                            navController.navigate("quizWrite")
                        } else if (quizType == QuizType.BOTH) {
                            if (Random.nextBoolean()) {
                                navController.navigate("quizWrite")
                            } else {
                                navController.navigate("quizChoose")
                            }
                        } else {
                            navController.navigate("quizChoose")
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = stringResource(id = R.string.start_quiz_btn))
                }
            }
        }
    }
}