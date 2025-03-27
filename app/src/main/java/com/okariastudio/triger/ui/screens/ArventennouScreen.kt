package com.okariastudio.triger.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.okariastudio.triger.R
import com.okariastudio.triger.ui.templates.ContactBottomSheet
import com.okariastudio.triger.ui.templates.StatisticsAccordion
import com.okariastudio.triger.viewmodel.SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArventennouScreen(settingsViewModel: SettingsViewModel, navController: NavHostController) {
    val versionNumber = getVersionNumber()
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val showBottomSheet = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val statistiques by settingsViewModel.statistiques.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        settingsViewModel.getStatistics(context)
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
                                settingsViewModel.toggleTheme()
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