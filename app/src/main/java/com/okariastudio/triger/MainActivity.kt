package com.okariastudio.triger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.okariastudio.triger.data.database.DatabaseProvider
import com.okariastudio.triger.data.firebase.FirebaseService
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.repository.GerRepository
import com.okariastudio.triger.ui.templates.GerList
import com.okariastudio.triger.ui.theme.TriGerTheme
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
            TriGerTheme {
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.tri_ger_du_jour),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (gersToday.isEmpty()) {
            item {
                Text(stringResource(id = R.string.no_ger_for_today), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
            items(gersToday) { ger: Ger ->
                GerList(listOf(ger), mainViewModel = mainViewModel, navController = navController)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.ton_brezhodex),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        if (gersBrezhodexDevezh.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.ger_du_jour),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            items(gersBrezhodexDevezh) { ger ->
                GerList(
                    gerList = listOf(ger),
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
        }

        if (gersBrezhodex.isNotEmpty()) {
            item {
                Text(
                    text = if (gersBrezhodexDevezh.isNotEmpty()) stringResource(id = R.string.autres_ger) else stringResource(id = R.string.ger_appris),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            items(gersBrezhodex) { ger ->
                GerList(
                    gerList = listOf(ger),
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
        } else if (gersBrezhodexDevezh.isEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.no_ger_brezhodex),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun ArventennouScreen() {
    Text(text = stringResource(id = R.string.arventennou_welcome), color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.fillMaxSize())
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TriGerTheme {
        Greeting("Android")
    }
}