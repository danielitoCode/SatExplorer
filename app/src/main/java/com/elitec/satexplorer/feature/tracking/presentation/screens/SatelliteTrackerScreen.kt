package com.elitec.satexplorer.feature.tracking.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elitec.satexplorer.feature.tracking.presentation.viewmodel.SatelliteInputViewModel
import com.elitec.satexplorer.feature.visualization.presentation.wrapper.GlobeScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun SatelliteTrackerScreen(
    modifier: Modifier = Modifier,
    viewModel: SatelliteInputViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var norad by remember { mutableStateOf("25544") }
    var name by remember { mutableStateOf("ISS") }
    var l1 by remember { mutableStateOf("") }
    var l2 by remember { mutableStateOf("") }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    Column(modifier = modifier.fillMaxSize()) {
        GlobeScreen(modifier = Modifier.fillMaxSize())

        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = bottomSheetState
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Seguimiento de satélite", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(value = norad, onValueChange = { norad = it }, label = { Text("NORAD ID") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { viewModel.loadFromApi(norad) }, modifier = Modifier.fillMaxWidth()) { Text("Cargar desde CelesTrak") }
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = l1, onValueChange = { l1 = it }, label = { Text("TLE línea 1") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = l2, onValueChange = { l2 = it }, label = { Text("TLE línea 2") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { viewModel.loadManual(name, l1, l2) }, modifier = Modifier.fillMaxWidth()) { Text("Usar TLE manual") }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (state.isLoading) CircularProgressIndicator()
                    state.satellite?.let { Text("Activo: ${it.name} (#${it.noradId})") }
                    state.error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }
                }
            }
        }
    }
}