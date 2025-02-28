package com.okariastudio.triger

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.okariastudio.triger.data.model.QuizLimit
import com.okariastudio.triger.data.model.QuizTarget
import com.okariastudio.triger.data.model.QuizType
import com.okariastudio.triger.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizStart(mainViewModel: MainViewModel, navController: NavHostController) {
    var quizTarget by remember { mutableStateOf(QuizTarget.ALL_WORDS) }
    var quizLimitType by remember { mutableStateOf(QuizLimit.NO_LIMIT) }
    var sliderWordPosition by remember { mutableFloatStateOf(0f) }
    var quizType by remember { mutableStateOf(QuizType.BOTH) }

    val totalGeriouLearned by mainViewModel.totalGeriouLearned.observeAsState(initial = 10)

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
                    onClick = { },
                    modifier = Modifier.padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Text(text = stringResource(id = R.string.start_quiz_btn))
                }
            }
        }
    }
}