package com.okariastudio.triger.ui.nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.okariastudio.triger.R

@Composable
fun BottomNavBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        val currentDestination by navController.currentBackStackEntryAsState()
        val currentRoute = currentDestination?.destination?.route

        BottomNavigationItem(
            selected = currentRoute == "Deskiñ",
            onClick = { navController.navigate("deskiñ") },
            icon = { Icon(
                ImageVector.vectorResource(id = R.drawable.ic_deskin),
                contentDescription = "Deskiñ",
                tint = if (currentRoute == "Deskiñ") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary)},
            label = { Text(text="Deskiñ", color = if (currentRoute == "Deskiñ") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary) },
            selectedContentColor = MaterialTheme.colorScheme.tertiary,
            unselectedContentColor = MaterialTheme.colorScheme.secondary
        )
        BottomNavigationItem(
            selected = currentRoute == "Brezhodex",
            onClick = { navController.navigate("brezhodex") },
            icon = { Icon(
                ImageVector.vectorResource(id = R.drawable.ic_brezhodex),
                contentDescription = "Brezhodex",
                tint = if (currentRoute == "Brezhodex") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
            ) },
            label = { Text("Brezhodex", color = if (currentRoute == "Brezhodex") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary) },
            selectedContentColor = MaterialTheme.colorScheme.tertiary,
            unselectedContentColor = MaterialTheme.colorScheme.secondary
        )
        /*
        BottomNavigationItem(
            selected = currentRoute == "arventennoù",
            onClick = { navController.navigate("arventennoù") },
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_arventennou), contentDescription = "Arventennoù") },
            label = { Text("Arventennoù") }
        )*/
    }
}