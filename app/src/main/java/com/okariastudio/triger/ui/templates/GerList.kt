package com.okariastudio.triger.ui.templates

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.viewmodel.MainViewModel

@Composable
fun GerList(
    gerList: List<Ger>,
    mainViewModel: MainViewModel,
    navController: NavHostController,
) {
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
