package com.okariastudio.triger.ui.templates

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.okariastudio.triger.R
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactBottomSheet(onDismiss: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxSize(),
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.contact),
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = message.value,
                onValueChange = { message.value = it },
                label = { Text(text = stringResource(R.string.message)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                coroutineScope.launch {
                    val success = sendForm(email.value, message.value)
                    if (success) {
                        Toast.makeText(context, "Message envoyé !", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Échec de l'envoi du message", Toast.LENGTH_SHORT)
                            .show()
                    }
                    onDismiss()
                }
            }) {
                Text(text = stringResource(R.string.send))
            }
        }
    }
}

suspend fun sendForm(email: String, message: String): Boolean {
    val apiService = RetrofitInstance.api
    return try {
        val response = apiService.sendContactForm(ContactRequest(email, message))
        response.isSuccessful
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

data class ContactRequest(val email: String, val message: String)

interface ContactApi {
    @POST("f/manqjjdr")
    suspend fun sendContactForm(@Body request: ContactRequest): Response<Unit>
}

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://formspree.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ContactApi = retrofit.create(ContactApi::class.java)
}