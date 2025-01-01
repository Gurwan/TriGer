package com.okariastudio.triger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.okariastudio.triger.data.database.DatabaseProvider
import com.okariastudio.triger.data.firebase.FirebaseService
import com.okariastudio.triger.data.repository.GerRepository
import com.okariastudio.triger.ui.templates.GerList
import com.okariastudio.triger.ui.theme.UnDevezhTriGerTheme
import com.okariastudio.triger.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }

        val gerRepository = GerRepository(
            gerDao = DatabaseProvider.getDatabase(this).gerDao(),
            firebaseService = FirebaseService()
        )
        val mainViewModel = MainViewModel(gerRepository, this.getSharedPreferences("Geriou", MODE_PRIVATE))

        lifecycleScope.launch {
            mainViewModel.synchronizeData()
            delay(3000)
            keepSplashScreen = false
        }
        enableEdgeToEdge()

        setContent {
            UnDevezhTriGerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        mainViewModel = mainViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun DeskinScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    val gersToday by mainViewModel.gersToday.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        mainViewModel.fetchGersForToday()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Tri ger du jour",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (gersToday.isEmpty()) {
                Text("Pas de mots pour aujourd'hui", style = MaterialTheme.typography.bodyMedium)
            } else {
                GerList(gersToday, mainViewModel = mainViewModel, navController = navController)
            }
        }
    }
}

@Composable
fun BrezhodexScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    val gersBrezhodex by mainViewModel.gersBrezhodex.observeAsState(emptyList())
    val gersBrezhodexDevezh by mainViewModel.gersBrezhodexDevezh.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        mainViewModel.fetchGersInBrezhodex()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Ton Brezhodex",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if(gersBrezhodexDevezh.isNotEmpty()){
                Text(
                    text = "Ger du jour",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                GerList(gersBrezhodexDevezh, mainViewModel = mainViewModel, navController = navController)
            }

            if(gersBrezhodex.isNotEmpty()){
                Text(
                    text = if(gersBrezhodexDevezh.isNotEmpty()) {"Autres ger" } else { "Ger appris" },
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                GerList(gersBrezhodex, mainViewModel = mainViewModel, navController = navController)
            } else {
                Text("Pas de mots dans le Brezhodex", style = MaterialTheme.typography.bodyMedium)
            }
        }

    }
}

@Composable
fun ArventennouScreen() {
    Text(text = "Bienvenue dans les paramètres!", modifier = Modifier.fillMaxSize())
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnDevezhTriGerTheme {
        Greeting("Android")
    }
}