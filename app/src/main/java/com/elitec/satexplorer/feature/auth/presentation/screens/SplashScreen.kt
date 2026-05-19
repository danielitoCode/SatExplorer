package com.elitec.satexplorer.feature.auth.presentation.screens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.R
import com.elitec.satexplorer.infrastructure.presentation.navigation.MainRoutes
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateTo: (MainRoutes) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(null) {
        delay(2000)
        navigateTo(MainRoutes.Landing)
    }
    Box(
        modifier = modifier
    ) {
        /*Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.banner),
                    contentDescription = "banner"
                )
            }
        }*/
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Surface(
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 8.dp,
                tonalElevation = 5.dp
            ) {
                Image(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "App icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(130.dp)
                )
            }
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = "Designed by: Elitec"
            )
        }
    }

}