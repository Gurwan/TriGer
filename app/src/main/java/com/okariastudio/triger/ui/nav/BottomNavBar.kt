package com.okariastudio.triger.ui.nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.okariastudio.triger.R

@Composable
fun BottomNavBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val currentDestination by navController.currentBackStackEntryAsState()
        val currentRoute = currentDestination?.destination?.route

        BottomNavigationItem(
            selected = currentRoute == "Deskiñ",
            onClick = { navController.navigate("deskiñ") },
            icon = {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_deskin),
                    contentDescription = stringResource(id = R.string.deskin),
                    tint = if (currentRoute == "Deskiñ") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.onBackground,
            unselectedContentColor = MaterialTheme.colorScheme.tertiary
        )
        BottomNavigationItem(
            selected = currentRoute == "Brezhodex",
            onClick = { navController.navigate("brezhodex") },
            icon = {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_brezhodex),
                    contentDescription = stringResource(id = R.string.brezhodex),
                    tint = if (currentRoute == "Brezhodex") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.onBackground,
            unselectedContentColor = MaterialTheme.colorScheme.tertiary
        )
        BottomNavigationItem(
            selected = currentRoute == "Quiz",
            onClick = { navController.navigate("quiz") },
            icon = {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_quiz),
                    contentDescription = stringResource(id = R.string.brezhodex),
                    tint = if (currentRoute == "Quiz") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.onBackground,
            unselectedContentColor = MaterialTheme.colorScheme.tertiary
        )
        BottomNavigationItem(
            selected = currentRoute == "Arventennoù",
            onClick = { navController.navigate("arventennoù") },
            icon = {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_arventennou),
                    contentDescription = stringResource(id = R.string.arventennoù),
                    tint = if (currentRoute == "Arventennoù") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.onBackground,
            unselectedContentColor = MaterialTheme.colorScheme.tertiary
        )
    }
}