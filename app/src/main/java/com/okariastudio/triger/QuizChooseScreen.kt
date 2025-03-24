package com.okariastudio.triger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.model.Quiz

@Composable
fun QuizScreen(quizItem: Quiz, onNext: () -> Unit) {

    val words = remember(
        quizItem.words,
        quizItem.exactWord
    ) { (quizItem.words + quizItem.exactWord).shuffled() }
    var selectedWord by remember { mutableStateOf<Ger?>(null) }
    var showNextButton by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = R.string.quiz_question,
                quizItem.exactWord?.french ?: ""
            ),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        words.forEach { word: Ger? ->
            val isCorrect = word?.breton == quizItem.exactWord?.breton
            Button(
                onClick = {
                    selectedWord = word
                    showNextButton = isCorrect
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    when {
                        selectedWord == null -> MaterialTheme.colorScheme.surface
                        selectedWord == word && isCorrect -> MaterialTheme.colorScheme.outline
                        selectedWord == word -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                if (word != null) {
                    Text(
                        text = word.breton,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }

        if (showNextButton) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    selectedWord = null
                    showNextButton = false
                    onNext()
                },
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = stringResource(id = R.string.suivant), color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
