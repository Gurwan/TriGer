package com.okariastudio.triger.ui.templates

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.okariastudio.triger.R
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.ui.theme.TriGerTheme

@Composable
fun GerCard(ger: Ger, modifier: Modifier = Modifier, onDeskinClick: (Ger) -> Unit = {}) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ger.breton,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { onDeskinClick(ger) },
                    modifier = Modifier.padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    if (ger.isLearned) {
                        Text(text = stringResource(id = R.string.reviser))
                    } else {
                        Text(text = stringResource(id = R.string.apprendre))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_french_flag),
                    contentDescription = "Drapeau français",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = ger.french,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                if (ger.isLearned) {
                    Text(
                        text = stringResource(
                            id = R.string.niveau,
                            if (ger.levelLearnings == 0) {
                                '1'
                            } else {
                                ger.levelLearnings.toString()
                            }
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = ger.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Exemple : ${ger.example}",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGerCard() {
    TriGerTheme(mainViewModel = null){
        GerCard(
            ger = Ger(
                id = "1",
                breton = "Kig-ha-farz",
                french = "Potée bretonne",
                description = "Un plat traditionnel de Bretagne.",
                levelLearnings = 1,
                isLearned = false,
                example = "Me o deus debret ur kig-ha-farz brav er Sul-mañ !"
            ),
            onDeskinClick = { ger -> println("Deskin clicked for ${ger.breton}") }
        )
    }
}

