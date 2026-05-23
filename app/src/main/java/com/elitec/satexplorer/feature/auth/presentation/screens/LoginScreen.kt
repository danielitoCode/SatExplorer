package com.elitec.satexplorer.feature.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.R
import com.elitec.satexplorer.feature.auth.presentation.model.AuthenticationProvider
import com.elitec.satexplorer.infrastructure.presentation.navigation.MainRoutes
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme

@Composable
fun LoginScreen(
    navigateTo: (MainRoutes) -> Unit,
    onAuthenticate: (provider: AuthenticationProvider, email: String, password: String) -> Unit,
    authInProgress: Boolean,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 8.dp,
            tonalElevation = 5.dp
        ) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.icon2),
                contentDescription = "App icon",
                modifier = Modifier.size(140.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = "Welcome Back"
            )
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = "Awaiting authorization for SATEXPLORER uplink"
            )
        }
        Surface(
            shadowElevation = 3.dp,
            tonalElevation = 3.dp,
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.padding(vertical = 3.dp),
                                imageVector = Icons.Default.AlternateEmail,
                                contentDescription = "email icon"
                            )
                            Text(
                                text = "EMAIL or USERNAME"
                            )
                        }
                    },
                    shape = RoundedCornerShape(15.dp)
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = pass,
                    onValueChange = { pass = it },
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.padding(vertical = 3.dp),
                                imageVector = Icons.Default.Lock,
                                contentDescription = "lock"
                            )
                            Text(
                                text = "PASSWORD"
                            )
                        }
                    },
                    shape = RoundedCornerShape(15.dp)
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onAuthenticate(AuthenticationProvider.ClerkPassword, email, pass)
                    }
                ) {
                    Text(
                        text = "LOGIN"
                    )
                }
                Text(
                    text = "External Authentication"
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            enabled = !authInProgress,
                            onClick = {
                                onAuthenticate(AuthenticationProvider.ClerkGoogle, email, pass)
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "GOOGLE"
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            enabled = !authInProgress,
                            onClick = {
                                onAuthenticate(AuthenticationProvider.ClerkGithub, email, pass)
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "GITHUB"
                                )
                            }
                        }
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don´t have an account?"
            )
            Text(
                modifier = Modifier.clickable {
                    navigateTo(MainRoutes.Register)
                },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                text = "Sign Up"
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Esto varia con la calidad de la señal y calibracion por las APIS
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Green,
                    modifier = Modifier.size(10.dp),
                    shape = CircleShape
                ) { }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Uplink:"
                    )
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = "Stable"
                    )
                }
            }
            // Esto varia con la calidad de la señal y calibracion por las APIS
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Green,
                    modifier = Modifier.size(10.dp),
                    shape = CircleShape
                ) { }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Server:"
                    )
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = "LEG 1"
                    )
                }
            }
            // Esto varia con la calidad de la señal y calibracion por las APIS
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Red,
                    modifier = Modifier.size(10.dp),
                    shape = CircleShape
                ) { }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Latency:"
                    )
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = "240ms"
                    )
                }
            }
        }
    }
}
