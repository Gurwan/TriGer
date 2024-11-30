package com.okariastudio.undevezhtriger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.okariastudio.undevezhtriger.viewmodel.MainViewModel

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    LaunchedEffect(Unit) {
        mainViewModel.synchronizeData()
    }

    Greeting(
        name = "Android",
        modifier = modifier
    )
}

