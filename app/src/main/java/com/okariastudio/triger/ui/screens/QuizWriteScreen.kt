package com.okariastudio.triger.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.okariastudio.triger.R
import com.okariastudio.triger.data.model.Quiz

@Composable
fun QuizWriteScreen(
    quizItem: Quiz,
    onNext: () -> Unit,
    stop: () -> Unit,
    modifier: Modifier = Modifier,
    unlimitedQuiz: Boolean = false,
) {
    var userInput by remember { mutableStateOf("") }
    var isCorrectAnswer by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.quiz_write_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = quizItem.exactWord?.french ?: stringResource(id = R.string.kargan),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (quizItem.exactWord != null) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                val correctWord = quizItem.exactWord.breton
                val inputLetters = userInput.padEnd(correctWord.length).take(correctWord.length)

                for (index in correctWord.indices) {
                    val letter = inputLetters.getOrNull(index) ?: ' '
                    val isCorrect = letter.equals(correctWord[index], ignoreCase = true)

                    Text(
                        text = letter.toString(),
                        color = when {
                            letter == ' ' -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            isCorrect -> MaterialTheme.colorScheme.outline
                            else -> MaterialTheme.colorScheme.error
                        },
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = {
                Text(
                    text = stringResource(id = R.string.ta_reponse),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = !userInput.equals(
                quizItem.exactWord?.breton ?: "",
                ignoreCase = true
            ) && userInput.isNotEmpty()
        )

        Button(
            onClick = {
                val correctWord = quizItem.exactWord?.breton ?: ""
                isCorrectAnswer = userInput.equals(correctWord, ignoreCase = true)
            },
            modifier = Modifier.padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.submit))
        }

        if (isCorrectAnswer) {
            Text(
                text = stringResource(id = R.string.bonne_reponse),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (unlimitedQuiz) {
                    Button(
                        onClick = {
                            userInput = ""
                            isCorrectAnswer = false
                            stop()
                        },
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.end_quiz_btn),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Button(
                    onClick = {
                        userInput = ""
                        isCorrectAnswer = false
                        onNext()
                    },
                    modifier = Modifier.padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.suivant),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
