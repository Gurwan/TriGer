package com.okariastudio.triger.ui.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.okariastudio.triger.R
import com.okariastudio.triger.data.model.Ger

@Composable
fun FilterRange(
    gers: List<Ger>,
    currentRange: IntRange,
    onDismiss: () -> Unit,
    onApplyRange: (IntRange) -> Unit
) {
    val minLevel = gers.minOfOrNull { it.levelLearnings } ?: 0
    val maxLevel = gers.maxOfOrNull { it.levelLearnings } ?: 100
    var filterRange by remember { mutableStateOf(minLevel..maxLevel) }
    val initialRangeStart = if (currentRange.first > -2) currentRange.first else minLevel
    var rangeStart by remember { mutableIntStateOf(initialRangeStart) }
    val initialRangeEnd = if (currentRange.last > -1) currentRange.last else maxLevel
    var rangeEnd by remember { mutableIntStateOf(initialRangeEnd) }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(text = stringResource(id = R.string.filter_title))
        },

        text = {
            Column {
                Text(
                    text = stringResource(id = R.string.select_level),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Niveau : $rangeStart - $rangeEnd",
                    style = MaterialTheme.typography.bodyMedium
                )

                RangeSlider(
                    value = rangeStart.toFloat()..rangeEnd.toFloat(),

                    valueRange = minLevel.toFloat()..maxLevel.toFloat(),

                    onValueChange = { range ->
                        rangeStart = range.start.toInt()
                        rangeEnd = range.endInclusive.toInt()
                    },

                    onValueChangeFinished = {
                        filterRange = rangeStart..rangeEnd
                    }
                )
            }
        },

        confirmButton = {
            TextButton(onClick = { onApplyRange(filterRange) }) {
                Text(text = stringResource(id = R.string.apply))
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}