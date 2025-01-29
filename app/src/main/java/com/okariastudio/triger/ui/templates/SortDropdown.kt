package com.okariastudio.triger.ui.templates

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.okariastudio.triger.R
import com.okariastudio.triger.data.model.SortOption

@Composable
fun SortDropdown(
    onDismiss: () -> Unit,
    onApplySort: (SortOption) -> Unit,
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            onClick = { onApplySort(SortOption.BRETON) },
            text = { Text(text = stringResource(id = R.string.sort_by_breton), color = MaterialTheme.colorScheme.onSurface) },
        )
        DropdownMenuItem(
            onClick = { onApplySort(SortOption.FRANCAIS) },
            text = { Text(text = stringResource(id = R.string.sort_by_francais), color = MaterialTheme.colorScheme.onSurface) }
        )
        DropdownMenuItem(
            onClick = { onApplySort(SortOption.DATE) },
            text = { Text(text = stringResource(id = R.string.sort_by_date), color = MaterialTheme.colorScheme.onSurface) }
        )
        DropdownMenuItem(
            onClick = { onApplySort(SortOption.LEVEL) },
            text = { Text(text = stringResource(id = R.string.sort_by_level), color = MaterialTheme.colorScheme.onSurface) }
        )
    }
}