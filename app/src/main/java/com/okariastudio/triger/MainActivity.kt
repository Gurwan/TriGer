package com.okariastudio.triger

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.okariastudio.triger.data.database.DatabaseProvider
import com.okariastudio.triger.data.firebase.FirebaseService
import com.okariastudio.triger.data.firebase.Tracking
import com.okariastudio.triger.data.model.SortOption
import com.okariastudio.triger.data.repository.GerRepository
import com.okariastudio.triger.ui.templates.ContactBottomSheet
import com.okariastudio.triger.ui.templates.FilterRange
import com.okariastudio.triger.ui.templates.GerList
import com.okariastudio.triger.ui.templates.SortDropdown
import com.okariastudio.triger.ui.templates.StatisticsAccordion
import com.okariastudio.triger.ui.theme.TriGerTheme
import com.okariastudio.triger.viewmodel.MainViewModel
import com.okariastudio.triger.worker.NotificationWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val preferences by lazy {
        getSharedPreferences("Geriou", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }

        //daily notification
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_notification_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(getDelay(), TimeUnit.MILLISECONDS)
                .build()
        )

        val gerRepository = GerRepository(
            gerDao = DatabaseProvider.getDatabase(this).gerDao(),
            firebaseService = FirebaseService()
        )
        val mainViewModel =
            MainViewModel(gerRepository, preferences)

        val launch = preferences.getInt("launch_count", 0) + 1
        preferences.edit().putInt("launch_count", launch).apply()

        lifecycleScope.launch {
            mainViewModel.synchronizeData()
            delay(2000)
            mainViewModel.fetchGersForToday()
            delay(1000)
            keepSplashScreen = false
        }
        enableEdgeToEdge()

        if (launch % 5 == 0) {
            val manager = ReviewManagerFactory.create(this)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println(task.result)
                } else {
                    @ReviewErrorCode val reviewErrorCode = (task.getException() as ReviewException).errorCode
                    println(reviewErrorCode)
                }
            }
        }

        setContent {
            TriGerTheme(mainViewModel = mainViewModel) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        mainViewModel = mainViewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        preferences.edit().putString("lastDayOpened", LocalDate.now().toString()).apply()
    }

    private fun getDelay(): Long {
        val now = LocalTime.now().toNanoOfDay() / 1_000_000
        val notificationTime = LocalTime.of(18, 30).toNanoOfDay() / 1_000_000

        return if (notificationTime > now) {
            notificationTime - now
        } else {
            24 * 60 * 60 * 1000 - (now - notificationTime)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeskinScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    val gersToday by mainViewModel.gersToday.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        mainViewModel.fetchGersForToday()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.tri_ger_du_jour))
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
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
                item {
                    GerList(
                        gersToday,
                        mainViewModel = mainViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrezhodexScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    tracking: Tracking
) {
    val gersBrezhodex by mainViewModel.gersBrezhodex.observeAsState(emptyList())
    val gersBrezhodexDevezh by mainViewModel.gersBrezhodexDevezh.observeAsState(emptyList())
    val (filterValue, setFilterValue) = remember { mutableStateOf<IntRange?>(null) }
    val isMinimalView by mainViewModel.isMinMode.collectAsState()

    var isRangeDialogOpen by remember { mutableStateOf(false) }
    var isSortDialogOpen by remember { mutableStateOf(false) }
    var currentRange by remember { mutableStateOf(IntRange(-2, -1)) }
    var sortOption by remember { mutableStateOf(SortOption.DATE_NEWEST) }

    LaunchedEffect(Unit) {
        mainViewModel.fetchGersInBrezhodex()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.ton_brezhodex))
                },
                actions = {
                    IconButton(onClick = { isRangeDialogOpen = true }) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_filter),
                            contentDescription = stringResource(id = R.string.filter)
                        )
                    }
                    IconButton(onClick = { mainViewModel.toggleMinMode() }) {
                        if (isMinimalView) {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                contentDescription = stringResource(id = R.string.display)
                            )
                        } else {
                            Icon(
                                ImageVector.vectorResource(id = R.drawable.ic_app),
                                contentDescription = stringResource(id = R.string.display)
                            )
                        }
                    }
                    IconButton(onClick = { isSortDialogOpen = true }) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_sort),
                            contentDescription = stringResource(id = R.string.sort)
                        )
                    }
                    if (isSortDialogOpen && !isRangeDialogOpen) {
                        SortDropdown(
                            onDismiss = { isSortDialogOpen = false },
                            onApplySort = { option ->
                                sortOption = Utils.changeSortOption(sortOption, option)
                                tracking.logSortApply(option)
                                isSortDialogOpen = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
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
                item {
                    GerList(
                        gerList = gersBrezhodexDevezh,
                        mainViewModel = mainViewModel,
                        navController = navController,
                        minimal = isMinimalView
                    )
                }
            }

            val filteredGersBrezhodex = filterValue?.let { range ->
                gersBrezhodex.filter { ger -> ger.levelLearnings in range }
            } ?: gersBrezhodex

            val sortedGersBrezhodex = when (sortOption) {
                SortOption.BRETON_AZ -> filteredGersBrezhodex.sortedBy { it.breton }
                SortOption.BRETON_ZA -> filteredGersBrezhodex.sortedByDescending { it.breton }
                SortOption.LEVEL_ASC -> filteredGersBrezhodex.sortedBy { it.levelLearnings }
                SortOption.LEVEL_DESC -> filteredGersBrezhodex.sortedByDescending { it.levelLearnings }
                SortOption.DATE_NEWEST -> filteredGersBrezhodex.sortedByDescending { it.lastLearningDate }
                SortOption.DATE_OLDEST -> filteredGersBrezhodex.sortedBy { it.lastLearningDate }
                SortOption.FRANCAIS_AZ -> filteredGersBrezhodex.sortedBy { it.french }
                SortOption.FRANCAIS_ZA -> filteredGersBrezhodex.sortedByDescending { it.french }
                SortOption.LEVEL -> filteredGersBrezhodex.sortedBy { it.levelLearnings }
                SortOption.DATE -> filteredGersBrezhodex.sortedByDescending { it.lastLearningDate }
                SortOption.BRETON -> filteredGersBrezhodex.sortedBy { it.breton }
                SortOption.FRANCAIS -> filteredGersBrezhodex.sortedBy { it.french }
            }

            if (sortedGersBrezhodex.isNotEmpty()) {
                item {
                    Text(
                        text = if (sortedGersBrezhodex.isNotEmpty()) stringResource(id = R.string.autres_ger) else stringResource(
                            id = R.string.ger_appris
                        ),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                item {
                    GerList(
                        gerList = sortedGersBrezhodex,
                        mainViewModel = mainViewModel,
                        navController = navController,
                        minimal = isMinimalView
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

        if (isRangeDialogOpen && !isSortDialogOpen) {
            FilterRange(
                gers = gersBrezhodex,
                onDismiss = { isRangeDialogOpen = false },
                currentRange = currentRange,
                onApplyRange = { range ->
                    currentRange = range
                    tracking.logFilterApply(range.first, range.last)
                    setFilterValue(range)
                    isRangeDialogOpen = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArventennouScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    val versionNumber = getVersionNumber()
    val isDarkTheme by mainViewModel.isDarkTheme.collectAsState()
    val showBottomSheet = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val statistiques by mainViewModel.statistiques.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        mainViewModel.getStatistics(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.arventennoù))
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showBottomSheet.value = true }) {
                    Icon(Icons.Default.Email, contentDescription = stringResource(R.string.contact))
                }
            }
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    StatisticsAccordion(statistiques)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
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
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                ImageVector.vectorResource(id = R.drawable.ic_heol),
                                contentDescription = "Mode Clair",
                                tint = if (isDarkTheme) Color.Gray else MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = stringResource(id = R.string.heol),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = if (isDarkTheme) Color.Gray else MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = {
                                mainViewModel.toggleTheme()
                                navController.navigate("arventennoù")
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
                                tint = if (isDarkTheme) MaterialTheme.colorScheme.primary else Color.Gray,
                            )
                            Text(
                                text = stringResource(id = R.string.loar),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = if (isDarkTheme) MaterialTheme.colorScheme.onBackground else Color.Gray
                            )
                        }
                    }
                }

                item {
                    val uriPolicy = "https://www.gurwan.com/apps/tri-ger-privacy-policy.html"
                    val string = stringResource(id = R.string.privacy_policy)
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

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                annotatedString
                                    .getStringAnnotations(
                                        tag = "URL",
                                        start = 0,
                                        end = string.length
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        val intent =
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(annotation.item)
                                            )
                                        context.startActivity(intent)
                                    }
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            BasicText(
                                text = annotatedString,
                                style = MaterialTheme.typography.headlineSmall,
                                color = colorLink
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                modifier = Modifier.padding(10.dp, 0.dp),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
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
    }

    if (showBottomSheet.value) {
        ContactBottomSheet(
            onDismiss = { showBottomSheet.value = false; }
        )
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
    TriGerTheme(mainViewModel = null) {
        Greeting("Android")
    }
}