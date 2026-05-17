package com.elitec.satexplorer.feature.visualization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitec.satexplorer.feature.visualization.domain.caseuse.BuildSceneGraphUseCase
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.model.VisualizationControlsState
import com.elitec.satexplorer.feature.visualization.presentation.util.RenderStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VisualizationViewModel(
    private val buildSceneGraph: BuildSceneGraphUseCase,
    private val stateHolder: RenderStateHolder
) : ViewModel() {

    private val _renderObjects =
        MutableStateFlow<List<RenderObject>>(emptyList())

    val renderObjects = _renderObjects.asStateFlow()

    private val _controlsState = MutableStateFlow(ControlsState())
    val controlsState = _controlsState.asStateFlow()


    init {
        viewModelScope.launch {
            renderObjects.collect { objects ->

                val scene = buildSceneGraph(objects)

                stateHolder.sceneGraph = scene
            }
        }
    }

    fun setObjects(objects: List<RenderObject>) {
        _renderObjects.value = objects
    }

    fun rotateLeft() {
        _controlsState.value = _controlsState.value.copy(yaw = _controlsState.value.yaw - 8f)
    }

    fun rotateRight() {
        _controlsState.value = _controlsState.value.copy(yaw = _controlsState.value.yaw + 8f)
    }

    fun rotateUp() {
        _controlsState.value = _controlsState.value.copy(pitch = (_controlsState.value.pitch + 6f).coerceIn(-85f, 85f))
    }

    fun rotateDown() {
        _controlsState.value = _controlsState.value.copy(pitch = (_controlsState.value.pitch - 6f).coerceIn(-85f, 85f))
    }

    fun updateZoom(distance: Float) {
        _controlsState.value = _controlsState.value.copy(cameraDistance = distance.coerceIn(1.25f, 10f))
    }

    data class ControlsState(
        val yaw: Float = 0f,
        val pitch: Float = 18f,
        val cameraDistance: Float = 3.2f
    )
}