package com.okariastudio.undevezhtriger.ui.templates

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.okariastudio.undevezhtriger.data.model.Ger
import com.okariastudio.undevezhtriger.viewmodel.MainViewModel

@Composable
fun GerList(gerList: List<Ger>, modifier: Modifier = Modifier, mainViewModel: MainViewModel, navController: NavHostController,) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = gerList,
            key = { ger -> ger.id }
        ) { ger ->
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
