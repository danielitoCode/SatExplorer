package com.elitec.satexplorer.feature.visualization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitec.satexplorer.feature.visualization.domain.caseuse.BuildSceneGraphUseCase
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
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
}