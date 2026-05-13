package com.elitec.satexplorer.feature.visualization.presentation.util

import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraph

class RenderStateHolder {
    @Volatile
    var sceneGraph: SceneGraph = SceneGraph(emptyList())
}