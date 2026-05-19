package com.elitec.satexplorer.feature.auth.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.R
import com.elitec.satexplorer.infrastructure.presentation.navigation.MainRoutes
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme
import com.elitec.satexplorer.infrastructure.presentation.theme.signalAmber
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import com.elitec.satexplorer.infrastructure.presentation.theme.telemetryRed
import com.elitec.satexplorer.infrastructure.presentation.theme.textSecondary

@Composable
fun RegistrationScreen(
    navigateTo: (MainRoutes) -> Unit,
    modifier: Modifier = Modifier
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var passConfirm by rememberSaveable { mutableStateOf("") }
    var securityEntropyLevel by remember {
        mutableStateOf<SecurityEntropy>(SecurityEntropy.Low("The password is to short"))
    }
    val securityLevelInfo = when (securityEntropyLevel) {
        is SecurityEntropy.Low -> Pair(telemetryRed, "LOW")
        SecurityEntropy.Medium -> Pair(signalAmber, "MEDIUM")
        SecurityEntropy.Optimal -> Pair(signalGreen, "OPTIMAL")
    }
    val animatedSecurityEntropyColor by animateColorAsState(
        targetValue = securityLevelInfo.first
    )

    var termsChecked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pass) {
        securityEntropyLevel = setSecurityLevel(pass)
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                style = MaterialTheme.typography.headlineLarge,
                text = "Registration zone"
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shadowElevation = 5.dp,
            tonalElevation = 5.dp,
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = { userName = it },
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
                                text = "USERNAME"
                            )
                        }
                    },
                    shape = RoundedCornerShape(15.dp)
                )
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
                                imageVector = Icons.Default.Mail,
                                contentDescription = "email icon"
                            )
                            Text(
                                text = "EMAIL"
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
                                imageVector = Icons.Default.Password,
                                contentDescription = "email icon"
                            )
                            Text(
                                text = "PASSWORD"
                            )
                        }
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 5.dp,
                    tonalElevation = 3.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                text = "SECURITY ENTROPY"
                            )
                            AnimatedContent(
                                targetState = securityLevelInfo.second
                            ) { securityLevelInfoLabel ->
                                Text(
                                    color = animatedSecurityEntropyColor,
                                    text = securityLevelInfoLabel
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = animateColorAsState(
                                    if (pass.isEmpty()) {
                                        MaterialTheme.colorScheme.surfaceContainer
                                    } else {
                                        animatedSecurityEntropyColor
                                    }
                                ).value ,
                                modifier = Modifier
                                    .height(10.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp)
                            ) { }
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = animateColorAsState(
                                    if (pass.length < 2) {
                                        MaterialTheme.colorScheme.surfaceContainer
                                    } else {
                                        animatedSecurityEntropyColor
                                    }
                                ).value ,
                                modifier = Modifier
                                    .height(10.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp)
                            ) { }
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = animateColorAsState(
                                    if (pass.length < 5) {
                                        MaterialTheme.colorScheme.surfaceContainer
                                    } else {
                                        animatedSecurityEntropyColor
                                    }
                                ).value ,
                                modifier = Modifier
                                    .height(10.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp)
                            ) { }
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = animateColorAsState(
                                    if (pass.length < 8) {
                                        MaterialTheme.colorScheme.surfaceContainer
                                    } else {
                                        animatedSecurityEntropyColor
                                    }
                                ).value ,
                                modifier = Modifier
                                    .height(10.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp)
                            ) { }
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = animateColorAsState(
                                    if (pass.length < 10) {
                                        MaterialTheme.colorScheme.surfaceContainer
                                    } else {
                                        animatedSecurityEntropyColor
                                    }
                                ).value ,
                                modifier = Modifier
                                    .height(10.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp)
                            ) { }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AnimatedVisibility(
                                visible = pass.length < 2
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = "error icon",
                                        tint = telemetryRed
                                    )
                                    Text(
                                        color = telemetryRed,
                                        text = "Password to short"
                                    )
                                }
                            }
                            AnimatedVisibility(
                                visible = pass.length < 5
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = "error icon",
                                        tint = telemetryRed
                                    )
                                    Text(
                                        color = telemetryRed,
                                        text = "Password most by contains numbers"
                                    )
                                }
                            }
                            AnimatedVisibility(
                                visible = pass.length < 8
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = "error icon",
                                        tint = signalAmber
                                    )
                                    Text(
                                        color = signalAmber,
                                        text = "For more security add special characters"
                                    )
                                }
                            }
                        }
                    }
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passConfirm,
                    onValueChange = { passConfirm = it },
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.padding(vertical = 3.dp),
                                imageVector = Icons.Default.Password,
                                contentDescription = "email icon"
                            )
                            Text(
                                text = "CONFIRM PASSWORD"
                            )
                        }
                    },
                    shape = RoundedCornerShape(15.dp)
                )
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = termsChecked,
                        onCheckedChange = { termsChecked = it }
                    )
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Justify,
                        text = "I agree to the GALACTIC TERMS OF SERVICES and acknowledge telemetry data collection protocol."
                    )
                }
            }

        }
        Column {
            Button(
                enabled = termsChecked && securityEntropyLevel !is SecurityEntropy.Low,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                onClick = { navigateTo(MainRoutes.Home("userRegistered")) }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.parabolical),
                        contentDescription = "parabolical receptor",
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "CREATE ACCOUNT"
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already registered?"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    modifier = Modifier.clickable {
                        navigateTo(MainRoutes.Login)
                    },
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    text = "Access terminal"
                )
            }
        }

    }
}

private fun validateForm(
    securityEntropy: SecurityEntropy,
    termCheck: Boolean,
    userName: String,
    email: String
): Boolean {

    if (email.isEmpty()) return false
    if (userName.isEmpty()) return false
    return termCheck && securityEntropy !is SecurityEntropy.Low
}

private fun setSecurityLevel(pass: String): SecurityEntropy {
    if (pass.length < 5) return SecurityEntropy.Low("Password to short")
    if (pass.length <= 5 || pass.length < 8 ) return SecurityEntropy.Medium
    return SecurityEntropy.Optimal
}


private sealed class SecurityEntropy {
    object Optimal: SecurityEntropy()
    object Medium: SecurityEntropy()
    class Low(message: String): SecurityEntropy()
}

@Preview(
    showBackground = true
)
@Composable
fun RegistrationScreenPreview() {
    SatExplorerTheme(
        darkTheme = false,
        dynamicColor = true
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            RegistrationScreen(
                navigateTo = {},
                modifier = Modifier.fillMaxSize()
            )
        }

    }
}
