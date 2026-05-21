package com.elitec.satexplorer.feature.visualization.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun RenderControl(
    onUpClick: () -> Unit,
    onDownClick: () -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onTrackPositionClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RenderControlButton(
            onRepeat = onLeftClick,
            shape = RoundedCornerShape(
                topStart = 10.dp,
                bottomStart = 10.dp
            ),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RenderControlButton(
                onRepeat = onUpClick,
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp
                ),
                icon = Icons.Default.KeyboardArrowUp
            )
            RenderControlButton(
                onRepeat = onTrackPositionClick,
                shape = RectangleShape,
                icon = Icons.Default.GpsFixed
            )
            RenderControlButton(
                onRepeat = onDownClick,
                shape = RoundedCornerShape(
                    bottomStart = 10.dp,
                    bottomEnd = 10.dp
                ),
                icon = Icons.Default.KeyboardArrowDown
            )
        }
        RenderControlButton(
            onRepeat = onRightClick,
            shape = RoundedCornerShape(
                topEnd = 10.dp,
                bottomEnd = 10.dp
            ),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight
        )
    }
}

@Composable
private fun RenderControlButton(
    onRepeat: () -> Unit,
    icon: ImageVector,
    intervalMs: Long = 33L,
    shape: Shape
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onRepeat()
            while (isPressed) {
                delay(intervalMs)
                onRepeat()
            }
        }
    }

    Surface(
        shape = shape,
        border = BorderStroke(
            1.dp, MaterialTheme.colorScheme.primary
        ),
        color = MaterialTheme.colorScheme.primary.copy(0.2f),
        modifier = Modifier.size(30.dp),
        onClick = onRepeat,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "arrow right icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun RenderControlPreview() {
    RenderControl(
        {},
        {},
        {},
        {},
        {}
    )
}