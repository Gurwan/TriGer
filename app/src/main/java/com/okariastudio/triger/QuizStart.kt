package com.okariastudio.triger

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.okariastudio.triger.data.model.QuizLimit
import com.okariastudio.triger.data.model.QuizTarget
import com.okariastudio.triger.data.model.QuizType
import com.okariastudio.triger.viewmodel.MainViewModel

@Composable
fun QuizStart(mainViewModel: MainViewModel, navController: NavHostController) {
    var quizTarget by remember { mutableStateOf(QuizTarget.ALL_WORDS) }
    var quizLimitType by remember { mutableStateOf(QuizLimit.NO_LIMIT) }
    var wordCountRange by remember { mutableStateOf(1..10) } //nombre de mots en base!!
    var quizTime by remember { mutableStateOf(0L) }
    var quizType by remember { mutableStateOf(QuizType.BOTH) }

    val totalGeriou by mainViewModel.totalGeriou.observeAsState(initial = 10)

    LaunchedEffect(totalGeriou) {
        wordCountRange = 1..totalGeriou
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = stringResource(id = R.string.target_quiz_label))
        QuizTarget.entries.forEach { target ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = quizTarget == target, onClick = { quizTarget = target })
                Text(target.toString())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.limit_quiz_label))
        QuizLimit.entries.forEach { type ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = quizLimitType == type, onClick = { quizLimitType = type })
                Text(type.toString())
            }
        }

        when (quizLimitType) {
            QuizLimit.N_WORDS -> {
                Text(text = stringResource(id = R.string.words_limit_quiz))
                Slider(value = wordCountRange.start.toFloat(),
                    valueRange = 1f..100f,
                    onValueChange = { wordCountRange = it.toInt()..wordCountRange.endInclusive })
            }

            QuizLimit.X_MINUTES -> {
                Text(text = stringResource(id = R.string.time_limit_quiz))
                TimePicker(quizTime) { quizTime = it }
            }

            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.type_quiz_label))
        QuizType.entries.forEach { type ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = quizType == type, onClick = { quizType = type })
                Text(type.toString())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text(text = stringResource(id = R.string.start_quiz_btn))
        }
    }
}

@Composable
fun TimePicker(selectedTime: Long, onTimeSelected: (Long) -> Unit) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context, { _, hours, minutes ->
            onTimeSelected(hours * 3600L + minutes * 60L)
        }, 0, 0, true
    )
    Button(onClick = { timePickerDialog.show() }) {
        Text(text = stringResource(id = R.string.time_limit_choose_quiz))
    }
}