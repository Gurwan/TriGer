package com.okariastudio.undevezhtriger

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.okariastudio.undevezhtriger.data.model.Ger
import com.okariastudio.undevezhtriger.data.model.Quiz
import com.okariastudio.undevezhtriger.viewmodel.MainViewModel

@Composable
fun QuizScreen(mainViewModel: MainViewModel, quizItem: Quiz, onNext: () -> Unit) {

    val words = remember(quizItem.words, quizItem.exactWord) { (quizItem.words + quizItem.exactWord).shuffled() }
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
            text = "Choisir la bonne traduction pour le mot ${quizItem.exactWord?.french}:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 24.dp)
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
                        selectedWord == null -> MaterialTheme.colorScheme.primary
                        selectedWord == word && isCorrect -> Color.Green
                        selectedWord == word -> Color.Red
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            ) {
                if (word != null) {
                    Text(text = word.breton, color = Color.White)
                }
            }
        }

        if (showNextButton) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onNext) {
                Text("Da-heul")
            }
        }
    }
}
