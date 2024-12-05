package com.okariastudio.undevezhtriger.ui.templates

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.okariastudio.undevezhtriger.data.model.Ger

@Composable
fun GerList(gerList: List<Ger>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = gerList,
            key = { ger -> ger.id }
        ) { ger ->
            GerCard(ger = ger)
        }
    }
}
