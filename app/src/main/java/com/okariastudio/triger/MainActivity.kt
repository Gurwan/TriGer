package com.okariastudio.triger

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
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
        val mainViewModel =
            MainViewModel(gerRepository, this.getSharedPreferences("Geriou", MODE_PRIVATE))


        lifecycleScope.launch {
            mainViewModel.synchronizeData()
            delay(2000)
            mainViewModel.fetchGersForToday()
            delay(1000)
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
                Text(
                    stringResource(id = R.string.no_ger_for_today),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
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
                    text = if (gersBrezhodexDevezh.isNotEmpty()) stringResource(id = R.string.autres_ger) else stringResource(
                        id = R.string.ger_appris
                    ),
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
    val versionNumber = getVersionNumber()
    val isDarkTheme = isSystemInDarkTheme()
    var darkTheme by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.arventennoù),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            val uriPolicy = "https://www.gurwan.com/apps/tri-ger-privacy-policy.html"
            val string = stringResource(id = R.string.privacy_policy)
            val context = LocalContext.current
            val annotatedString = buildAnnotatedString {
                append(stringResource(id = R.string.privacy_policy))
                addStringAnnotation(
                    tag = "URL",
                    annotation = uriPolicy,
                    start = 0,
                    end = string.length
                )
            }

            val colorSchemeLink = MaterialTheme.colorScheme.onBackground
            val colorLink: () -> Color = { colorSchemeLink }

            BasicText(
                text = annotatedString,
                modifier = Modifier
                    .clickable {
                        annotatedString
                            .getStringAnnotations(
                                tag = "URL",
                                start = 0,
                                end = string.length
                            )
                            .firstOrNull()
                            ?.let { annotation ->
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                context.startActivity(intent)
                            }
                    }
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = colorLink
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.theme_change),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_heol),
                        contentDescription = "Mode Clair",
                        tint = if (darkTheme) Color.Gray else MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(id = R.string.heol),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = if (darkTheme) Color.Gray else MaterialTheme.colorScheme.onBackground
                    )
                }

                Switch(
                    checked = darkTheme,
                    onCheckedChange = {
                        darkTheme = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_loar),
                        contentDescription = "Mode Sombre",
                        tint = if (darkTheme) MaterialTheme.colorScheme.primary else Color.Gray,
                    )
                    Text(
                        text = stringResource(id = R.string.loar),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = if (darkTheme) MaterialTheme.colorScheme.onBackground else Color.Gray
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.version_number, versionNumber),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

    }
}

@Composable
fun getVersionNumber(): String {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName
    versionName?.let {
        return it
    }
    return stringResource(id = R.string.inconnu)
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