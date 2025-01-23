package com.okariastudio.triger.ui.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.viewmodel.MainViewModel

@Composable
fun GerList(
    gerList: List<Ger>,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    minimal: Boolean = false
) {

    if (minimal) {
        val rows = gerList.chunked(2)

        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                row.forEach { ger ->
                    GerMinCard(
                        ger = ger,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        onDeskinClick = { clickedGer ->
                            mainViewModel.fetchWrongGersForQuiz(clickedGer.id)
                            navController.navigate("quizChoose")
                            println("Deskin clicked for ${clickedGer.breton}")
                        }
                    )
                }

                repeat(2 - row.size) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    )
                }
            }
        }
    } else {
        gerList.forEach { ger ->
            GerCard(
                ger = ger,
                onDeskinClick = { clickedGer ->
                    mainViewModel.fetchWrongGersForQuiz(clickedGer.id)
                    navController.navigate("quizChoose")
                    println("Deskin clicked for ${clickedGer.breton}")
                }
            )
        }
    }
}

