package com.elitec.satexplorer.infrastructure.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.infrastructure.presentation.model.NavBarItem
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme

@Composable
fun BottomNavBar(
    navItems: List<NavBarItem>,
    selectedItemName: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 5.dp,
        tonalElevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
                .padding(
                    vertical = 5.dp,
                    horizontal = 10.dp
                )
        ) {
            items(navItems) { navItem ->

                val animatedContentColor by animateColorAsState(
                    if(selectedItemName == navItem.tittle) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Box(
                    modifier = Modifier.clickable {
                        navItem.action()
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            tint = animatedContentColor,
                            imageVector = navItem.icon,
                            contentDescription = "navItem button icon"
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            color = animatedContentColor,
                            text = navItem.tittle
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    val navItems = listOf(
        NavBarItem("Dashboard", Icons.Default.Dashboard, action = {}),
        NavBarItem("Orbit", Icons.Default.RadioButtonUnchecked, action = {}),
        NavBarItem("Search", Icons.Default.Search, action = {}),
        NavBarItem("Sky", Icons.Default.RemoveRedEye, action = {}),
        NavBarItem("Profile", Icons.Default.AccountCircle, action = {}),
    )
    SatExplorerTheme {
        BottomNavBar(
            navItems = navItems,
            selectedItemName = "Dashboard",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        )
    }
}