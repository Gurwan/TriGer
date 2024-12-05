package com.okariastudio.undevezhtriger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.okariastudio.undevezhtriger.data.database.DatabaseProvider
import com.okariastudio.undevezhtriger.data.firebase.FirebaseService
import com.okariastudio.undevezhtriger.data.model.Ger
import com.okariastudio.undevezhtriger.data.repository.GerRepository
import com.okariastudio.undevezhtriger.ui.templates.GerList
import com.okariastudio.undevezhtriger.ui.theme.UnDevezhTriGerTheme
import com.okariastudio.undevezhtriger.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gerRepository = GerRepository(
            gerDao = DatabaseProvider.getDatabase(this).gerDao(),
            firebaseService = FirebaseService()
        )
        val mainViewModel = MainViewModel(gerRepository)

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
fun DeskinScreen(mainViewModel: MainViewModel) {
    val sampleGers = listOf(
        Ger(
            id = "1",
            breton = "Kalon",
            french = "Cœur",
            description = "Organe principal de la circulation sanguine chez les êtres vivants.",
            example = "Me 'gav din eo bet boulc'het ma c'halon gant ho mouezh.",
            isLearned = true
        ),
        Ger(
            id = "2",
            breton = "Bugel",
            french = "Enfant",
            description = "Personne jeune, entre la naissance et l'adolescence.",
            example = "Ar bugel a zo o c'hoarzhin dindan an heol.",
            isLearned = false
        )
    )

    GerList(gerList = sampleGers)
}

@Composable
fun BrezhodexScreen() {
    Text(text = "Bienvenue dans Brezhodex!", modifier = Modifier.fillMaxSize())
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