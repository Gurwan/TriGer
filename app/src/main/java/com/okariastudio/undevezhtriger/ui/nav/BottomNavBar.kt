package com.okariastudio.undevezhtriger.ui.nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.okariastudio.undevezhtriger.R

@Composable
fun BottomNavBar(navController: NavHostController) {
    BottomNavigation {
        val currentDestination by navController.currentBackStackEntryAsState()
        val currentRoute = currentDestination?.destination?.route

        BottomNavigationItem(
            selected = currentRoute == "Deskiñ",
            onClick = { navController.navigate("deskiñ") },
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_deskin), contentDescription = "Deskiñ") },
            label = { Text("Deskiñ") }
        )
        BottomNavigationItem(
            selected = currentRoute == "Brezhodex",
            onClick = { navController.navigate("brezhodex") },
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_brezhodex), contentDescription = "Brezhodex") },
            label = { Text("Brezhodex") }
        )
        BottomNavigationItem(
            selected = currentRoute == "arventennoù",
            onClick = { navController.navigate("arventennoù") },
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_arventennou), contentDescription = "Arventennoù") },
            label = { Text("Arventennoù") }
        )
    }
}