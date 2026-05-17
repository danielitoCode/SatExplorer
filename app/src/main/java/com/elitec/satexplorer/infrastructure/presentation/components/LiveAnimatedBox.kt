package com.elitec.satexplorer.infrastructure.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import kotlinx.coroutines.delay

@Composable
fun LiveAnimatedBox(
    modifier: Modifier = Modifier
) {
    // Animations
    var expandedInternalSphere by rememberSaveable { mutableStateOf(false) }
    val expandedInternalSphereSate = if (expandedInternalSphere) 0.dp else 5.dp
    val animatableInternalSphereSize by animateDpAsState(
        targetValue = expandedInternalSphereSate,
        animationSpec = tween(durationMillis = 12000, easing = FastOutSlowInEasing)
    )

    var expandedIntermediumSphere by rememberSaveable { mutableStateOf(false) }
    val expandedIntermediumSphereSate = if (expandedInternalSphere) 0.dp else 10.dp
    val animatableIntermediumSphereSize by animateDpAsState(
        targetValue = expandedIntermediumSphereSate,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    var expandedExternalSphere by rememberSaveable { mutableStateOf(false) }
    val expandedExternalSphereSate = if (expandedInternalSphere) 0.dp else 20.dp
    val animatableExternalSphereSize by animateDpAsState(
        targetValue = expandedExternalSphereSate,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )
    LaunchedEffect(expandedInternalSphere) {
        delay(1200)
        expandedInternalSphere = !expandedInternalSphere
    }
    LaunchedEffect(expandedIntermediumSphere) {
        delay(800)
        expandedIntermediumSphere = !expandedIntermediumSphere
    }
    LaunchedEffect(expandedExternalSphere) {
        delay(400)
        expandedExternalSphere = !expandedExternalSphere
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = signalGreen.copy(0.2f)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(30.dp)
            ) {
                Surface(
                    color = signalGreen.copy(0.4f),
                    shape = CircleShape,
                    modifier = Modifier.size(animatableExternalSphereSize)
                ) { }
                Surface(
                    color = signalGreen.copy(0.7f),
                    shape = CircleShape,
                    modifier = Modifier.size(animatableIntermediumSphereSize)
                ) { }

                Surface(
                    color = signalGreen,
                    shape = CircleShape,
                    modifier = Modifier.size(animatableInternalSphereSize)
                ) { }
            }
            Text(
                color = signalGreen,
                text = "Live"
            )
        }
    }
}
