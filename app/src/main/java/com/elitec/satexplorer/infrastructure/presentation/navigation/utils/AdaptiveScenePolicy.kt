package com.elitec.satexplorer.infrastructure.presentation.navigation.utils

interface AdaptiveScenePolicy {
    fun enableDualPane(route: Any, spec: AdaptiveLayoutSpec): Boolean
}

object DefaultAdaptiveScenePolicy : AdaptiveScenePolicy {
    override fun enableDualPane(route: Any, spec: AdaptiveLayoutSpec): Boolean {
        return spec.showListAndDetail
    }
}